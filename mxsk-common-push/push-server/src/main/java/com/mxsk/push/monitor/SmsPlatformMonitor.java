package com.mxsk.push.monitor;

import com.mxsk.push.cache.AccessInfoMeta;
import com.mxsk.push.cache.MetaCache;
import com.mxsk.push.dto.SmsSignDTO;
import com.mxsk.push.dto.SmsTemplateDTO;
import com.mxsk.push.enums.AuditStatusEnum;
import com.mxsk.push.service.SmsPlatformService;
import com.mxsk.push.service.SmsSignService;
import com.mxsk.push.service.SmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 短信签名审核状态监听器
 *
 * @author: zhengguican
 * create in 2021/5/26 17:59
 */
@Component
public class SmsPlatformMonitor {

    @Autowired
    private MetaCache metaCache;

    @Autowired
    private SmsSignService smsSignService;

    @Autowired
    private SmsTemplateService smsTemplateService;

    @Autowired
    private SmsPlatformService smsPlatformService;

    /**
     * 查询待处理的短信签名 平台审核状态
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void smsSignStatusMonitor() {
        List<SmsSignDTO> smsSignDTOList = this.smsSignService.querySmsSignByAuditStatus(
                AuditStatusEnum.WAIT_AUDIT.getCode());
        smsSignDTOList.stream().forEach(item -> {
            AccessInfoMeta accessInfoMeta = (AccessInfoMeta) metaCache.get(item.getTenantId().toString());
            this.smsPlatformService.addQuerySignTask(item.getSignName(), accessInfoMeta);
        });
    }

    /**
     * 查询待处理的短信模版 平台审核状态
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void smsTemplateStatusMonitor() {
        List<SmsTemplateDTO> smsTemplateDTOList = this.smsTemplateService.queryTemplateByAuditStatus(AuditStatusEnum.WAIT_AUDIT.getCode());
        smsTemplateDTOList.stream().forEach(item -> {
            AccessInfoMeta accessInfoMeta = (AccessInfoMeta) metaCache.get(item.getTenantId().toString());
            this.smsPlatformService.addQueryTemplateTask(accessInfoMeta, item.getTemplateCode());
        });
    }
}
