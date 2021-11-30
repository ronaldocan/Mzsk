package com.mxsk.push.service;


import com.mxsk.push.cache.AccessInfoMeta;
import com.mxsk.push.dto.*;
import com.mxsk.push.model.SmsRecord;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 短信平台service
 *
 * @author: zhengguican
 * create in 2021/5/20 11:50
 */
public interface SmsPlatformService {

    /**
     * 发送短信
     *
     * @param sendSmsRequestDTO 发送短信请求DTO
     * @return
     */
    Result<Void> saveSms(SendSmsRequestDTO sendSmsRequestDTO);

    /**
     * 批量发送短信
     *
     * @param sendBatchSmsRequestDTO 批量发送短信请求DTO
     * @return
     */
    Result<Void> saveBatchSms(SendBatchSmsRequestDTO sendBatchSmsRequestDTO);

    /**
     * 添加短信模板
     *
     * @param addSmsTemplateRequestDTO 短信模板添加请求DTO
     * @return
     */
    Result<String> saveSmsTemplate(AddSmsTemplateRequestDTO addSmsTemplateRequestDTO);

    /**
     * 添加短信签名
     *
     * @param addSmsSignRequestDTO 短信签名添加请求DTO
     * @return
     */
    Result<Void> saveSmsSign(AddSmsSignRequestDTO addSmsSignRequestDTO);

    /**
     * 添加短信模板审核状态查询更新任务
     *
     * @param accessInfoMeta 平台账号访问信息
     * @param templateCode   短信模板编码
     */
    void addQueryTemplateTask(AccessInfoMeta accessInfoMeta, String templateCode);

    /**
     * 添加短信签名审核状态查询更新任务
     *
     * @param accessInfoMeta 平台账号访问信息
     * @param signName       签名
     */
    void addQuerySignTask(String signName, AccessInfoMeta accessInfoMeta);

    /**
     * 查询发送短信详情
     *
     * @param querySmsDetailRequestDTO 查询短信详情请求DTO
     * @return
     */
    Result<SmsRecord> querySmsDetail(QuerySmsDetailRequestDTO querySmsDetailRequestDTO);

    /**
     * 添加短信签名
     *
     * @param tenantId     租户ID
     * @param signName     签名
     * @param signSource   签名来源
     * @param remark       备注
     * @param signFileList 签名相关文件
     * @return
     */
    Result<Void> saveSmsSignWithFile(Long tenantId, String signName, String signSource, String remark, List<MultipartFile> signFileList);

    /**
     * 更新短信发送状态
     *
     * @param message
     * @return
     */
    Result<Void> updateSmsSendStatus(String message);
}
