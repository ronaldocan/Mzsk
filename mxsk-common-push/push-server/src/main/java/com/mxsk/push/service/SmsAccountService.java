package com.mxsk.push.service;

import com.mxsk.push.dto.AddSmsAccountRequestDTO;
import com.mxsk.push.dto.Result;

/**
 * 短信平台账号记录service
 *
 * @author: zhengguican
 * create in 2021/7/12 14:25
 */
public interface SmsAccountService {

    /**
     * 新增短信平台账号记录
     *
     * @param addSmsAccountRequestDTO 短信账号新增请求DTO
     * @return
     */
    Result<Void> save(AddSmsAccountRequestDTO addSmsAccountRequestDTO);
}
