package com.mxsk.push.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.mxsk.push.cache.AccessInfoMeta;
import com.mxsk.push.cache.MetaCache;
import com.mxsk.push.command.AbstractSmsCommand;
import com.mxsk.push.command.SmsEngine;
import com.mxsk.push.command.api.aliyun.*;
import com.mxsk.push.config.mq.PulsarTopics;
import com.mxsk.push.constant.SmsConstant;
import com.mxsk.push.constant.SmsPlatformConstant;
import com.mxsk.push.converter.SmsSignConverter;
import com.mxsk.push.dto.*;
import com.mxsk.push.enums.*;
import com.mxsk.push.mapper.SmsRecordMapper;
import com.mxsk.push.mapper.SmsSignMapper;
import com.mxsk.push.mapper.SmsTemplateMapper;
import com.mxsk.push.model.SmsRecord;
import com.mxsk.push.model.SmsSign;
import com.mxsk.push.model.SmsTemplate;
import com.mxsk.push.service.SmsPlatformService;
import com.mxsk.push.util.SmsUtil;
import io.github.majusko.pulsar.producer.PulsarTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @author: zhengguican
 * create in 2021/5/20 15:38
 */
@Service
@Slf4j
public class SmsPlatformServiceImpl implements SmsPlatformService {

    @Autowired
    private SmsEngine smsEngine;

    @Autowired
    private MetaCache metaCache;

    @Autowired
    private SmsTemplateMapper smsTemplateMapper;

    @Autowired
    private SmsRecordMapper smsRecordMapper;

    @Autowired
    private SmsSignMapper smsSignMapper;

    @Autowired
    private ScheduledThreadPoolExecutor smsTaskExecutor;

    @Autowired
    private PulsarTemplate<String> pulsarTemplate;


    @Override
    public Result<Void> saveSms(SendSmsRequestDTO sendSmsRequestDTO) {
        Date nowTime = new Date();
        Result result = checkSendSmsParamValid(sendSmsRequestDTO);
        if (!ResultCodeEnum.SUCCESS.getCode().equals(result.getCode())) {
            return result;
        }
        AccessInfoMeta accessInfoMeta = checkAccessInfoValid(sendSmsRequestDTO.getTenantId());
        AbstractSmsCommand sendSmsCommand = getSendSmsCommand(accessInfoMeta, sendSmsRequestDTO);
        Result<SmsResponseDTO> resultDTO = smsEngine.process(accessInfoMeta, sendSmsCommand);
        if (!ResultCodeEnum.SUCCESS.getCode().equals(resultDTO.getCode())) {
            return Result.error(ResultCodeEnum.FAIL.getCode(), resultDTO.getMessage());
        }
        SmsResponseDTO smsResponseDTO = resultDTO.getData();
        SmsRecord smsRecord = SmsRecord.builder().templateCode(sendSmsRequestDTO.getTemplateCode())
                .signName(sendSmsRequestDTO.getSignName()).phone(sendSmsRequestDTO.getPhoneNumber())
                .updateTime(nowTime).accountId(accessInfoMeta.getAccountId()).status(SmsRecordSendStatusEnum.WAIT_SEND.getCode())
                .bizId(smsResponseDTO.getBizId()).createTime(nowTime).build();
        this.smsRecordMapper.insert(smsRecord);
        // 添加查询短信发送状态任务
        this.addQuerySmsSendStatusTask(accessInfoMeta, List.of(smsRecord));
        return Result.success();
    }

    @Override
    public Result<Void> saveBatchSms(SendBatchSmsRequestDTO sendBatchSmsRequestDTO) {
        if (sendBatchSmsRequestDTO.getPhoneList().size() > SmsConstant.SMS_SEND_COUNT_LIMIT) {
            String errorMsg = String.format("发送短信数量超出上限，租户ID:%s，发送数量：%s", sendBatchSmsRequestDTO.getTenantId(),
                    sendBatchSmsRequestDTO.getPhoneList().size());
            log.error(errorMsg);
            return Result.error(ResultCodeEnum.FAIL.getCode(), "发送短信数量超出上限");
        }
        AccessInfoMeta accessInfoMeta = checkAccessInfoValid(sendBatchSmsRequestDTO.getTenantId());
        AbstractSmsCommand sendBatchSmsCommand = getSendBatchSmsCommand(accessInfoMeta, sendBatchSmsRequestDTO);
        Result<SmsResponseDTO> resultDTO = smsEngine.process(accessInfoMeta, sendBatchSmsCommand);
        if (!ResultCodeEnum.SUCCESS.getCode().equals(resultDTO.getCode())) {
            return Result.error(ResultCodeEnum.FAIL.getCode(), resultDTO.getMessage());
        }

        // 填充短信记录
        List<SmsRecord> smsRecordList = this.generateSmsRecordByRequestDTO(resultDTO.getData().getBizId(),
                accessInfoMeta.getAccountId(), sendBatchSmsRequestDTO);
        if (smsRecordList.size() > SmsConstant.DB_SAVE_SMS_BATCH_SIZE) {
            List<List<SmsRecord>> partRecordList = Lists.partition(smsRecordList, SmsConstant.DB_SAVE_SMS_BATCH_SIZE);
            partRecordList.forEach(list -> {
                this.smsRecordMapper.insertBatch(list);
            });
        } else {
            this.smsRecordMapper.insertBatch(smsRecordList);
        }
        // 添加查询短信发送状态任务
        this.addQuerySmsSendStatusTask(accessInfoMeta, smsRecordList);
        return Result.success();
    }

    @Override
    public Result<String> saveSmsTemplate(AddSmsTemplateRequestDTO addSmsTemplateRequestDTO) {
        Date nowTime = new Date();
        Result result = checkTemplateParam(addSmsTemplateRequestDTO);
        if (!ResultCodeEnum.SUCCESS.getCode().equals(result.getCode())) {
            return result;
        }
        AccessInfoMeta accessInfoMeta = checkAccessInfoValid(addSmsTemplateRequestDTO.getTenantId());

        // 填充请求命令
        AbstractSmsCommand smsCommand = getAddSmsTemplateCommand(accessInfoMeta, addSmsTemplateRequestDTO);
        Result<AddSmsTemplateResponseDTO> smsResponseDTO = smsEngine.process(accessInfoMeta, smsCommand);
        if (!ResultCodeEnum.SUCCESS.getCode().equals(smsResponseDTO.getCode())) {
            return Result.error(ResultCodeEnum.FAIL.getCode(), smsResponseDTO.getMessage());
        }
        // 模板编号
        String templateCode = smsResponseDTO.getData().getTemplateCode();

        SmsTemplate smsTemplate = SmsTemplate.builder().code(templateCode).accountId(accessInfoMeta.getAccountId()).
                name(addSmsTemplateRequestDTO.getTemplateName()).content(addSmsTemplateRequestDTO.getTemplateContent()).
                type(addSmsTemplateRequestDTO.getTemplateType()).createTime(nowTime).updateTime(nowTime).
                status(TemplateAuditStatusEnum.WAIT_AUDIT.getCode()).build();
        this.smsTemplateMapper.insert(smsTemplate);

        // 添加查询模板审核状态任务
        addQueryTemplateTaskInternal(accessInfoMeta, templateCode);
        return Result.success(templateCode);
    }

    @Override
    public Result<Void> saveSmsSign(AddSmsSignRequestDTO addSmsSignRequestDTO) {
        Date nowTime = new Date();
        Result result = checkAddSignParam(addSmsSignRequestDTO);
        if (!ResultCodeEnum.SUCCESS.getCode().equals(result.getCode())) {
            return result;
        }
        AccessInfoMeta accessInfoMeta = checkAccessInfoValid(addSmsSignRequestDTO.getTenantId());

        // 填充请求命令
        AbstractSmsCommand smsCommand = getAddSmsSignCommand(accessInfoMeta, addSmsSignRequestDTO);
        Result<SmsResponseDTO> smsResponseDTO = smsEngine.process(accessInfoMeta, smsCommand);
        if (!ResultCodeEnum.SUCCESS.getCode().equals(smsResponseDTO.getCode())) {
            return Result.error(ResultCodeEnum.FAIL.getCode(), smsResponseDTO.getMessage());
        }

        SmsSign smsSign = SmsSign.builder().signName(addSmsSignRequestDTO.getSignName()).status(SignAuditStatusEnum.WAIT_AUDIT.getCode())
                .accountId(accessInfoMeta.getAccountId()).signSource(addSmsSignRequestDTO.getSignSource())
                .remark(addSmsSignRequestDTO.getRemark()).updateTime(nowTime).createTime(nowTime).build();
        this.smsSignMapper.insert(smsSign);
        return Result.success();
    }

    @Override
    public Result<List<SmsSignQueryResponseDTO>> querySmsSign(SmsSignQueryRequestDTO smsSignQueryRequestDTO) {
        AccessInfoMeta accessInfoMeta = checkAccessInfoValid(smsSignQueryRequestDTO.getTenantId());
        QueryWrapper<SmsSign> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SmsSign::getAccountId, accessInfoMeta.getAccountId());
        List<SmsSign> smsSignList = this.smsSignMapper.selectList(queryWrapper);
        List<SmsSignQueryResponseDTO> smsSignQueryRequestDTOList = smsSignList.stream().map(item -> {
            return SmsSignConverter.INSTANCE.domain2QuerySignDTO(item);
        }).collect(Collectors.toList());
        return Result.success(smsSignQueryRequestDTOList);
    }

    @Override
    public void addQueryTemplateTask(AccessInfoMeta accessInfoMeta, String templateCode) {
        this.addQueryTemplateTaskInternal(accessInfoMeta, templateCode);
    }

    @Override
    public void addQuerySignTask(String signName, AccessInfoMeta accessInfoMeta) {
        this.submitSmsSignQueryTask(signName, accessInfoMeta);
    }

    @Override
    public Result<SmsRecord> querySmsDetail(QuerySmsDetailRequestDTO querySmsDetailRequestDTO) {
        AccessInfoMeta accessInfoMeta = (AccessInfoMeta) metaCache.get(querySmsDetailRequestDTO.getTenantId().toString());
        SmsUtil.checkAccessInfoNotEmpty(accessInfoMeta, querySmsDetailRequestDTO.getTenantId());
        // 填充请求命令
        AbstractSmsCommand smsCommand = getQuerySmsDetailCommand(accessInfoMeta, querySmsDetailRequestDTO);
        Result<QuerySmsDetailResponseDTO> smsResponseDTO = smsEngine.process(accessInfoMeta, smsCommand);
        if (!ResultCodeEnum.SUCCESS.getCode().equals(smsResponseDTO.getCode())) {
            return Result.error(ResultCodeEnum.FAIL.getCode(), smsResponseDTO.getMessage());
        }
        QuerySmsDetailResponseDTO querySmsDetailResponseDTO = smsResponseDTO.getData();
        QuerySmsDetailResponseDTO.SmsSendDetailDTO detailDTO = querySmsDetailResponseDTO.getSmsSendDetailDTOs().getSmsSendDetailDTO().get(0);
        SmsRecord smsRecord = SmsRecord.builder().bizId(querySmsDetailRequestDTO.getRequestId())
                .content(detailDTO.getContent()).phone(querySmsDetailRequestDTO.getPhoneNumber())
                .status(detailDTO.getSendStatus().toString()).build();
        return Result.success(smsRecord);
    }

    @Override
    public Result<Void> saveSmsSignWithFile(Long tenantId, String signName, String signSource, String remark, List<MultipartFile> signFileList) {
        List<SignFileDTO> signFileDTOList = signFileList.stream().map(item -> {
            String contentType = SmsUtil.getFileExtension(item);
            String encodeData;
            try {
                encodeData = Base64.encode(item.getBytes());
            } catch (IOException e) {
                log.error("SMS服务读取文件异常,租户ID：{},msg：{}",tenantId,e.getMessage());
                throw new RuntimeException(e);
            }
            return SignFileDTO.builder().fileContents(encodeData).fileSuffix(contentType).build();
        }).collect(Collectors.toList());

        AddSmsSignRequestDTO addSmsSignRequestDTO = AddSmsSignRequestDTO.builder().signFileList(signFileDTOList)
                .signName(signName).signSource(signSource).remark(remark).tenantId(tenantId).build();
        return this.saveSmsSign(addSmsSignRequestDTO);
    }

    @Override
    public Result<Void> updateSmsSendStatus(String message) {
        List<QuerySmsDetailRequestDTO> querySmsDetailRequestDTOList = JSONUtil.toList(message, QuerySmsDetailRequestDTO.class);
        // 需重新放入任务队列的记录
        List<QuerySmsDetailRequestDTO> waitSyncList = new ArrayList<>();
        // 待更新的发送短信记录
        List<SmsRecord> updateRecordList = new ArrayList<>();
        querySmsDetailRequestDTOList.stream().forEach(item -> {
            Result<SmsRecord> recordResult = this.querySmsDetail(item);
            if (!ResultCodeEnum.SUCCESS.getCode().equals(recordResult.getCode()) ||
                    SmsRecordSendStatusEnum.WAIT_SEND.getCode().equals(recordResult.getData().getStatus())) {
                waitSyncList.add(item);
            } else {
                updateRecordList.add(recordResult.getData());
            }
        });

        if (!CollectionUtils.isEmpty(updateRecordList)) {
            if (updateRecordList.size() > SmsConstant.DB_UPDATE_SMS_BATCH_SIZE) {
                List<List<SmsRecord>> subList = Lists.partition(updateRecordList, SmsConstant.DB_UPDATE_SMS_BATCH_SIZE);
                subList.stream().forEach(updateList -> {
                    this.smsRecordMapper.updateSmsSendStatusBatch(updateList);
                });
            } else {
                this.smsRecordMapper.updateSmsSendStatusBatch(updateRecordList);
            }
        }
        // 待更新的任务重新放入队列
        if (!CollectionUtils.isEmpty(waitSyncList)) {
            try {
                pulsarTemplate.send(PulsarTopics.QUERY_SMS_SEND_STATUS, JSONUtil.toJsonStr(waitSyncList));
            } catch (PulsarClientException e) {
                log.error("添加更新短信发送状态任务失败,data:{},msg:{}",waitSyncList, e);
                throw new RuntimeException(e);
            }
        }
        return Result.success();
    }

    /**
     * 添加短信模板审核状态查询更新任务
     *
     * @param accessInfoMeta 平台账号访问信息
     * @param templateCode   短信模板编码
     */
    private void addQueryTemplateTaskInternal(AccessInfoMeta accessInfoMeta, String templateCode) {
        smsTaskExecutor.schedule(new Runnable() {
            @Override
            public void run() {
                AbstractSmsCommand smsCommand = getSmsTemplateQueryCommand(accessInfoMeta, templateCode);
                Result<QueryTemplateResponseDTO> smsResponseDTO = smsEngine.process(accessInfoMeta, smsCommand);
                QueryTemplateResponseDTO aliyunQueryTemplateResponseDTO = smsResponseDTO.getData();
                if (!AliyunCodeEnum.OK.getCode().equals(aliyunQueryTemplateResponseDTO.getCode())) {
                    String errorMsg = String.format("短信平台请求模板命令失败,错误码：%s，描述：%s",
                            aliyunQueryTemplateResponseDTO.getCode(), aliyunQueryTemplateResponseDTO.getMessage());
                    log.error(errorMsg);
                    addQueryTemplateTaskInternal(accessInfoMeta, templateCode);
                } else if (TemplateAuditStatusEnum.WAIT_AUDIT.getCode().equals(aliyunQueryTemplateResponseDTO.getTemplateStatus())) {
                    addQueryTemplateTaskInternal(accessInfoMeta, templateCode);
                } else {

                    Date nowTime = new Date();
                    // 已处理好审核
                    String auditStatus = aliyunQueryTemplateResponseDTO.getTemplateStatus();
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("code", templateCode);
                    SmsTemplate smsTemplate = SmsTemplate.builder().status(auditStatus).updateTime(nowTime).build();
                    smsTemplateMapper.update(smsTemplate, queryWrapper);
                }
            }
        }, 30, TimeUnit.SECONDS);
    }

    /**
     * 添加短信模板审核状态查询更新任务
     *
     * @param accessInfoMeta 平台账号访问信息
     * @param signName       短信签名
     */
    private void submitSmsSignQueryTask(String signName, AccessInfoMeta accessInfoMeta) {
        smsTaskExecutor.schedule(new Runnable() {
            @Override
            public void run() {
                Date nowTime = new Date();
                AbstractSmsCommand smsCommand = getSmsSignQueryCommand(accessInfoMeta, signName);
                Result<QuerySignResponseDTO> smsResponseDTO = smsEngine.process(accessInfoMeta, smsCommand);
                QuerySignResponseDTO querySignResponseDTO = smsResponseDTO.getData();
                if (!AliyunCodeEnum.OK.getCode().equals(querySignResponseDTO.getCode())) {
                    String errorMsg = String.format("短信平台请求签名查询命令失败,错误码：%s，描述：%s",
                            querySignResponseDTO.getCode(), querySignResponseDTO.getMessage());
                    log.error(errorMsg);
                } else if (SignAuditStatusEnum.WAIT_AUDIT.getCode().equals(querySignResponseDTO.getSignStatus())) {
                    String infoMsg = String.format("短信平台请求查询签名命令成功(未审核),描述：%s",
                            querySignResponseDTO);
                    log.info(infoMsg);
                } else {

                    // 已处理好审核
                    String auditStatus = querySignResponseDTO.getSignStatus();
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("account_id", accessInfoMeta.getAccountId());
                    queryWrapper.eq("sign_name", signName);
                    SmsSign smsSign = SmsSign.builder().status(auditStatus).updateTime(nowTime).build();
                    smsSignMapper.update(smsSign, queryWrapper);
                }
            }
        }, 30, TimeUnit.SECONDS);
    }


    /**
     * 检验请求发送短信参数
     *
     * @param sendSmsRequestDTO
     * @return
     */
    private Result checkSendSmsParamValid(SendSmsRequestDTO sendSmsRequestDTO) {

        if (sendSmsRequestDTO.getTenantId() == null || StringUtils.isEmpty(sendSmsRequestDTO.getTemplateCode()) ||
                StringUtils.isEmpty(sendSmsRequestDTO.getPhoneNumber()) || StringUtils.isEmpty(sendSmsRequestDTO.getSignName())) {
            return Result.error(ResultCodeEnum.FAIL.getCode(), "参数异常");
        }
        return Result.success();
    }


    /**
     * 检验请求模板参数
     *
     * @param addSmsTemplateRequestDTO
     * @return
     */
    private Result checkTemplateParam(AddSmsTemplateRequestDTO addSmsTemplateRequestDTO) {

        if (addSmsTemplateRequestDTO.getTenantId() == null || addSmsTemplateRequestDTO.getTemplateName() == null ||
                addSmsTemplateRequestDTO.getTemplateContent() == null || addSmsTemplateRequestDTO.getTemplateType() == null) {
            return Result.error(ResultCodeEnum.FAIL.getCode(), "参数异常");
        }
        return Result.success();
    }

    /**
     * 检验请求模板参数
     *
     * @param addSmsSignRequestDTO
     * @return
     */
    private Result checkAddSignParam(AddSmsSignRequestDTO addSmsSignRequestDTO) {

        if (addSmsSignRequestDTO.getTenantId() == null || addSmsSignRequestDTO.getSignName() == null ||
                addSmsSignRequestDTO.getRemark() == null || addSmsSignRequestDTO.getSignSource() == null) {
            return Result.error(ResultCodeEnum.FAIL.getCode(), "参数异常");
        }
        return Result.success();
    }

    /**
     * 填充批量发送短信命令
     *
     * @param accessInfoMeta    短信平台访问信息
     * @param sendSmsRequestDTO 批量发送短信模板请求DTO
     * @return
     */
    private AbstractSmsCommand getSendSmsCommand(AccessInfoMeta accessInfoMeta, SendSmsRequestDTO sendSmsRequestDTO) {
        AbstractSmsCommand smsCommand = null;
        switch (accessInfoMeta.getPlatform()) {
            case SmsPlatformConstant.ALIYUN:
                smsCommand = AliyunSendSmsCommand.builder().phoneNumber(sendSmsRequestDTO.getPhoneNumber()).
                        paramMap(sendSmsRequestDTO.getParamMap()).signName(sendSmsRequestDTO.getSignName())
                        .templateCode(sendSmsRequestDTO.getTemplateCode()).build();
                break;
            default:
                break;
        }
        return smsCommand;
    }

    /**
     * 填充批量发送短信命令
     *
     * @param accessInfoMeta         短信平台访问信息
     * @param sendBatchSmsRequestDTO 批量发送短信模板请求DTO
     * @return
     */
    private AbstractSmsCommand getSendBatchSmsCommand(AccessInfoMeta accessInfoMeta, SendBatchSmsRequestDTO sendBatchSmsRequestDTO) {
        AbstractSmsCommand smsCommand = null;
        switch (accessInfoMeta.getPlatform()) {
            case SmsPlatformConstant.ALIYUN:
                smsCommand = AliyunSendBatchSmsCommand.builder().phoneList(sendBatchSmsRequestDTO.getPhoneList()).
                        paramList(sendBatchSmsRequestDTO.getParamList()).signNameList(sendBatchSmsRequestDTO.getSignNameList()).templateCode(sendBatchSmsRequestDTO.getTemplateCode())
                        .build();
                break;
            default:
                break;
        }
        return smsCommand;
    }

    /**
     * 填充添加短信模板签名命令
     *
     * @param accessInfoMeta           短信平台访问信息
     * @param addSmsTemplateRequestDTO 添加短信模板请求DTO
     * @return
     */
    private AbstractSmsCommand getAddSmsTemplateCommand(AccessInfoMeta accessInfoMeta, AddSmsTemplateRequestDTO addSmsTemplateRequestDTO) {
        AbstractSmsCommand smsCommand = null;
        switch (accessInfoMeta.getPlatform()) {
            case SmsPlatformConstant.ALIYUN:
                smsCommand = AliyunAddSmsTemplateCommand.builder().templateType(addSmsTemplateRequestDTO.getTemplateType()).
                        templateContent(addSmsTemplateRequestDTO.getTemplateContent()).templateName(addSmsTemplateRequestDTO.getTemplateName())
                        .remark(addSmsTemplateRequestDTO.getRemark()).build();
                break;
            default:
                break;
        }
        return smsCommand;
    }

    /**
     * 填充查询短信模板命令
     *
     * @param accessInfoMeta 短信平台访问信息
     * @param templateCode   模板编码
     * @return
     */
    private AbstractSmsCommand getSmsTemplateQueryCommand(AccessInfoMeta accessInfoMeta, String templateCode) {
        AbstractSmsCommand smsCommand = null;
        switch (accessInfoMeta.getPlatform()) {
            case SmsPlatformConstant.ALIYUN:
                smsCommand = AliyunQuerySmsTemplateCommand.builder().templateCode(templateCode).build();
                break;
            default:
                break;
        }
        return smsCommand;
    }

    /**
     * 填充查询短信签名命令
     *
     * @param accessInfoMeta 短信平台访问信息
     * @param signName       短信签名名称
     * @return
     */
    private AbstractSmsCommand getSmsSignQueryCommand(AccessInfoMeta accessInfoMeta, String signName) {
        AbstractSmsCommand smsCommand = null;
        switch (accessInfoMeta.getPlatform()) {
            case SmsPlatformConstant.ALIYUN:
                smsCommand = AliyunQuerySmsSignCommand.builder().signName(signName).build();
                break;
            default:
                break;
        }
        return smsCommand;
    }

    /**
     * 填充添加短信签名命令
     *
     * @param accessInfoMeta 短信平台访问信息
     * @param requestDTO     添加短信签名DTO
     * @return
     */
    private AbstractSmsCommand getAddSmsSignCommand(AccessInfoMeta accessInfoMeta, AddSmsSignRequestDTO requestDTO) {
        AbstractSmsCommand smsCommand = null;
        switch (accessInfoMeta.getPlatform()) {
            case SmsPlatformConstant.ALIYUN:
                smsCommand = AliyunAddSmsSignCommand.builder().signName(requestDTO.getSignName()).
                        signSource(requestDTO.getSignSource()).remark(requestDTO.getRemark()).signFileList(requestDTO.getSignFileList())
                        .build();
                break;
            default:
                break;
        }
        return smsCommand;
    }

    /**
     * 根据DTO 生成短信记录
     *
     * @param sendBatchSmsRequestDTO
     * @param accountId              短信平台ID
     * @param bizId                  平台请求序列号
     * @return
     */
    private List<SmsRecord> generateSmsRecordByRequestDTO(String bizId, Integer accountId,
                                                          SendBatchSmsRequestDTO sendBatchSmsRequestDTO) {

        Date nowTime = new Date();
        List<SmsRecord> list = new ArrayList<>();

        List<String> phoneNumberList = sendBatchSmsRequestDTO.getPhoneList();
        List<String> signNameList = sendBatchSmsRequestDTO.getSignNameList();

        for (int i = 0; i < sendBatchSmsRequestDTO.getPhoneList().size(); i++) {

            String phone = phoneNumberList.get(i);
            String signName = signNameList.get(i);
            SmsRecord smsRecord = SmsRecord.builder().templateCode(sendBatchSmsRequestDTO.getTemplateCode())
                    .signName(signName).accountId(accountId).phone(phone).createTime(nowTime)
                    .updateTime(nowTime).accountId(accountId).status(SmsRecordSendStatusEnum.WAIT_SEND.getCode())
                    .bizId(bizId).build();
            list.add(smsRecord);
        }

        return list;

    }

    /**
     * 填充查询短信详情命令
     * @param accessInfoMeta           短信平台访问信息
     * @param querySmsDetailRequestDTO 查询短信详情请求DTO
     * @return
     */
    private AbstractSmsCommand getQuerySmsDetailCommand(AccessInfoMeta accessInfoMeta, QuerySmsDetailRequestDTO querySmsDetailRequestDTO) {
        AbstractSmsCommand smsCommand = null;
        switch (accessInfoMeta.getPlatform()) {
            case SmsPlatformConstant.ALIYUN:
                smsCommand = AliyunQuerySendSmsDetailCommand.builder().currentPage(Integer.valueOf(1)).
                        pageSize(Integer.valueOf(1)).phoneNumber(querySmsDetailRequestDTO.getPhoneNumber())
                        .requestId(querySmsDetailRequestDTO.getRequestId()).sendDate(querySmsDetailRequestDTO.getSendDate()).build();
                break;
            default:
                break;
        }
        return smsCommand;
    }

    /**
     * 检验短信平台账号是否有效
     * @param tenantId 租户ID
     * @return
     */
    private AccessInfoMeta checkAccessInfoValid(Long tenantId) {
        AccessInfoMeta accessInfoMeta = (AccessInfoMeta) metaCache.get(tenantId.toString());
        SmsUtil.checkAccessInfoNotEmpty(accessInfoMeta, tenantId);
        return accessInfoMeta;
    }

    /**
     * 生成查询短信详情请求DTO
     * @param tenantId  租户ID
     * @param smsRecord 短信记录
     * @return
     */
    private QuerySmsDetailRequestDTO generateQuerySmsDetailDTO(Long tenantId, SmsRecord smsRecord) {
        String sendDate = DateUtil.format(smsRecord.getCreateTime(), "yyyyMMdd");
        QuerySmsDetailRequestDTO querySmsDetailRequestDTO = QuerySmsDetailRequestDTO.builder().requestId(smsRecord.getBizId())
                .phoneNumber(smsRecord.getPhone()).tenantId(tenantId).sendDate(sendDate).build();
        return querySmsDetailRequestDTO;
    }

    /**
     * 添加查询短信发送状态任务
     * @param accessInfoMeta 平台配置信息
     * @param smsRecordList  短信记录集合
     */
    private void addQuerySmsSendStatusTask(AccessInfoMeta accessInfoMeta, List<SmsRecord> smsRecordList) {
        List<QuerySmsDetailRequestDTO> smsDetailRequestDTOS = smsRecordList.stream().map(item -> {
            return generateQuerySmsDetailDTO(accessInfoMeta.getTenantId(), item);
        }).collect(Collectors.toList());
        try {
            this.pulsarTemplate.send(PulsarTopics.QUERY_SMS_SEND_STATUS, JSONUtil.toJsonStr(smsDetailRequestDTOS));
        } catch (PulsarClientException e) {
            log.error("添加查看短信发送状态任务失败,{}", e);
            throw new RuntimeException(e);
        }
    }
}
