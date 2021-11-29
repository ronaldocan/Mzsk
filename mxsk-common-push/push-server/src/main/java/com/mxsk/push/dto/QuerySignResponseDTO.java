package com.mxsk.push.dto;

import lombok.Data;

/**
 * 阿里查询短信签名接口响应DTO
 * @author: zhengguican
 * create in 2021/5/20 16:34
 */
@Data
public class QuerySignResponseDTO extends SmsResponseDTO {

    /**
     * 审核备注
     */
    private String Reason;

    /**
     * 签名审核状态
     */
    private String SignStatus;
}
