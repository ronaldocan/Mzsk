package com.mxsk.push.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mxsk.push.constant.AreaCodeConstant;
import com.mxsk.push.constant.SmsPlatformConstant;
import com.mxsk.push.dto.CreateSmsAccountRequestDTO;
import com.mxsk.push.dto.Result;
import com.mxsk.push.enums.SignAuditStatusEnum;
import com.mxsk.push.enums.SignSourceTypeEnum;
import com.mxsk.push.enums.SmsAccountStateEnum;
import com.mxsk.push.enums.TemplateAuditStatusEnum;
import com.mxsk.push.mapper.SmsAccountMapper;
import com.mxsk.push.mapper.SmsSignMapper;
import com.mxsk.push.mapper.SmsTemplateMapper;
import com.mxsk.push.model.SmsAccount;
import com.mxsk.push.model.SmsSign;
import com.mxsk.push.model.SmsTemplate;
import com.mxsk.push.service.SmsAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * @author: zhengguican
 * create in 2021/7/16 15:02
 */
@Service
public class SmsAccountServiceImpl implements SmsAccountService {

    @Autowired
    private SmsAccountMapper smsAccountMapper;

    @Autowired
    private SmsSignMapper smsSignMapper;

    @Autowired
    private SmsTemplateMapper smsTemplateMapper;

    @Value(value = "${tenant.account.signName}")
    private String signName;

    @Value(value = "${tenant.account.templateCode}")
    private String smsTemplateCode;

    @Value(value = "${tenant.account.templateCodeJp}")
    private String smsTemplateCodeJp;

    @Override
    public Result<Void> save(CreateSmsAccountRequestDTO createSmsAccountRequestDTO) {

        // TODO zgc 待需求演进，添加短信平台配置接口
        LambdaQueryWrapper<SmsAccount> countMapper = new QueryWrapper<SmsAccount>().lambda()
                .eq(SmsAccount::getTenantId, createSmsAccountRequestDTO.getTenantId());
        Integer count = this.smsAccountMapper.selectCount(countMapper);
        if (count > 0) {
            return Result.success();
        }

        Date nowTime = new Date();

        SmsAccount smsAccount = SmsAccount.builder().accessKey(SmsPlatformConstant.ACCESS_KEY)
                .secret(SmsPlatformConstant.SECRET).endPoint("aliyun_default").status(SmsAccountStateEnum.ENABLE.getStatus())
                .tenantId(createSmsAccountRequestDTO.getTenantId()).createTime(nowTime).updateTime(nowTime)
                .platform("aliyun")
                .build();

        this.smsAccountMapper.insert(smsAccount);
        SmsTemplate smsTemplate = this.getTenantDefaultTemplate(smsAccount.getId(), createSmsAccountRequestDTO.getAreaCode());
        this.smsTemplateMapper.insert(smsTemplate);

        SmsSign smsSign = this.getTenantDefaultSmsSign(smsAccount.getId());
        this.smsSignMapper.insert(smsSign);
        return Result.success();
    }

    /**
     * 检验新增记录参数是否有效
     *
     * @param createSmsAccountRequestDTO
     */
    private void checkCreateParamValid(CreateSmsAccountRequestDTO createSmsAccountRequestDTO) {

        Objects.requireNonNull(createSmsAccountRequestDTO.getAccessKey(), "平台账号访问key为空");
        Objects.requireNonNull(createSmsAccountRequestDTO.getPlatform(), "账号平台类型为空");
        Objects.requireNonNull(createSmsAccountRequestDTO.getSecret(), "平台账号访问密钥为空");
        Objects.requireNonNull(createSmsAccountRequestDTO.getTenantId(), "租户ID为空");
    }

    /**
     * 获取租户默认短信模板
     *
     * @param accountId 短信账号记录ID
     * @param areaCode  区号
     * @return
     */
    private SmsTemplate getTenantDefaultTemplate(Integer accountId, String areaCode) {
        Date nowTime = new Date();
        String templateCode = AreaCodeConstant.AREA_CODE_CHINA.equals(areaCode) ? smsTemplateCode : smsTemplateCodeJp;
        String content = AreaCodeConstant.AREA_CODE_CHINA.equals(areaCode) ? "尊敬的客户您好，您申请的账号已通过审核登录名：${username}，密码：${password}。如果问题请联系我们。"
                : "お客様各位、お申し込みいただいたアカウントが審査に合格しました。ログイン名：${username}、パスワード：${password}。 ご不明な点がございましたらお問い合わせください";

        SmsTemplate smsTemplate = SmsTemplate.builder()
                .accountId(accountId).code(templateCode).name("租户账密通知")
                .status(TemplateAuditStatusEnum.AUDIT_PASS.getCode())
                .content(content)
                .type("1").createTime(nowTime).updateTime(nowTime).build();
        return smsTemplate;
    }

    /**
     * 获取租户默认短信签名
     *
     * @param accountId 短信账号记录ID
     * @return
     */
    private SmsSign getTenantDefaultSmsSign(Integer accountId) {

        Date nowTime = new Date();
        SmsSign smsSign = SmsSign.builder().accountId(accountId)
                .signName(signName)
                .signSource(SignSourceTypeEnum.ENTERPRISE.getCode())
                .remark("temp")
                .status(SignAuditStatusEnum.AUDIT_PASS.getCode())
                .createTime(nowTime).updateTime(nowTime).build();
        return smsSign;
    }
}
