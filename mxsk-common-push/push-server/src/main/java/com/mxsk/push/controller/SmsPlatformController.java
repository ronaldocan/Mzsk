package com.mxsk.push.controller;

import com.mxsk.push.api.SmsPlatformApi;
import com.mxsk.push.dto.*;
import com.mxsk.push.service.SmsPlatformService;
import com.mxsk.push.service.SmsSignService;
import com.mxsk.push.service.SmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 短信平台请求接口
 * @author: zhengguican
 * create in 2021/5/20 13:46
 */
@RestController
public class SmsPlatformController implements SmsPlatformApi {

    @Autowired
    private SmsPlatformService smsPlatformService;

    @Autowired
    private SmsTemplateService smsTemplateService;

    @Autowired
    private SmsSignService smsSignService;

    @Override
    public Result<Void> sendSms(SendSmsRequestDTO sendSmsRequestDTO) {
        return this.smsPlatformService.saveSms(sendSmsRequestDTO);
    }

    @Override
    public Result<Void> sendBatchSms(SendBatchSmsRequestDTO sendBatchSmsRequestDTO) {
        return this.smsPlatformService.saveBatchSms(sendBatchSmsRequestDTO);
    }

    @Override
    public Result<String> addSmsTemplate(AddSmsTemplateRequestDTO addSmsTemplateRequestDTO) {
        return this.smsPlatformService.saveSmsTemplate(addSmsTemplateRequestDTO);
    }

    @Override
    public Result<Void> addSmsSign(AddSmsSignRequestDTO smsRequestDTO) {
        return this.smsPlatformService.saveSmsSign(smsRequestDTO);
    }

    @Override
    public Result<List<SmsTemplateQueryResponseDTO>> querySmsTemplate(SmsTemplateQueryRequestDTO smsTemplateQueryRequestDTO) {
        return this.smsTemplateService.querySmsTemplate(smsTemplateQueryRequestDTO);
    }

    @Override
    public Result<List<SmsSignQueryResponseDTO>> querySmsSign(SmsSignQueryRequestDTO smsSignQueryRequestDTO) {
        return this.smsSignService.querySmsSign(smsSignQueryRequestDTO);
    }

    @Override
    public Result<Void> addSmsSignWithFile(Long tenantId, String signName, String signSource, String remark, List<MultipartFile> signFileList) {
        return this.smsPlatformService.saveSmsSignWithFile(tenantId, signName, signSource, remark, signFileList);
    }
}
