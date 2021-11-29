package com.mxsk.push.service;

import com.mxsk.push.dto.SmsSignDTO;

import java.util.List;

/**
 * @author: zhengguican
 * create in 2021/5/28 9:34
 */
public interface SmsSignService {

    /**
     * 根据审核状态查找记录
     *
     * @param auditStatus 审核状态
     * @return
     */
    List<SmsSignDTO> querySmsSignByAuditStatus(String auditStatus);
}
