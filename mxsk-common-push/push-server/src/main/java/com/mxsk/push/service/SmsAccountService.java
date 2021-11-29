package com.mxsk.push.service;

import com.mxsk.push.dto.CreateSmsAccountRequestDTO;
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
     * @param createSmsAccountRequestDTO 短信账号新增请求DTO
     * @return
     */
    Result<Void> save(CreateSmsAccountRequestDTO createSmsAccountRequestDTO);
}
