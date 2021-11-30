package com.mxsk.push.service;

import com.mxsk.push.dto.Result;
import com.mxsk.push.dto.SmsSignDTO;
import com.mxsk.push.dto.SmsSignQueryRequestDTO;
import com.mxsk.push.dto.SmsSignQueryResponseDTO;

import java.util.List;

/**
 * 短信签名service
 * @author: zhengguican
 * create in 2021/5/28 9:34
 */
public interface SmsSignService {

    /**
     * 查询短信签名
     *
     * @param smsSignQueryRequestDTO
     * @return
     */
    Result<List<SmsSignQueryResponseDTO>> querySmsSign(SmsSignQueryRequestDTO smsSignQueryRequestDTO);


    /**
     * 根据审核状态查找记录
     *
     * @param auditStatus 审核状态
     * @return
     */
    List<SmsSignDTO> querySmsSignByAuditStatus(String auditStatus);
}
