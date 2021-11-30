package com.mxsk.push.service;

import com.mxsk.push.dto.Result;
import com.mxsk.push.dto.SmsTemplateDTO;
import com.mxsk.push.dto.SmsTemplateQueryRequestDTO;
import com.mxsk.push.dto.SmsTemplateQueryResponseDTO;

import java.util.List;

/**
 * 短信模板接口
 *
 * @author: zhengguican
 * create in 2021/5/26 17:01
 */
public interface SmsTemplateService {

    /**
     * 根据请求DTO查找模板记录
     *
     * @param smsTemplateQueryRequestDTO 查询短信模版请求DTO
     * @return
     */
    Result<List<SmsTemplateQueryResponseDTO>> querySmsTemplate(SmsTemplateQueryRequestDTO smsTemplateQueryRequestDTO);

    /**
     * 根据审核状态查找记录
     *
     * @param auditStatus 审核状态
     * @return
     */
    List<SmsTemplateDTO> queryTemplateByAuditStatus(String auditStatus);
}
