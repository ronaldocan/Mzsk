package com.mxsk.push.monitor;

import com.mxsk.push.cache.AccessInfoMeta;
import com.mxsk.push.cache.MetaCache;
import com.mxsk.push.dto.SmsTemplateDTO;
import com.mxsk.push.enums.TemplateAuditStatusEnum;
import com.mxsk.push.service.SmsPlatformService;
import com.mxsk.push.service.SmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 短信模版审核状态监听器
 *
 * @author: zhengguican
 * create in 2021/5/26 17:59
 */
@Component
public class SmsTemplateMonitor {

    @Autowired
    private MetaCache metaCache;

    @Autowired
    private SmsTemplateService smsTemplateService;

    @Autowired
    private SmsPlatformService smsPlatformService;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void smsTemplateStatusMonitor() {
        List<SmsTemplateDTO> smsTemplateDTOList = this.smsTemplateService.queryTemplateByAuditStatus(TemplateAuditStatusEnum.WAIT_AUDIT.getCode());
        smsTemplateDTOList.stream().forEach(item -> {
            AccessInfoMeta accessInfoMeta = (AccessInfoMeta) metaCache.get(item.getTenantId().toString());
            this.smsPlatformService.addQueryTemplateTask(accessInfoMeta, item.getTemplateCode());
        });
    }
}
