package com.mxsk.push.dto;

import lombok.Data;

/**
 * 短信签名记录dto
 * @author: zhengguican
 * create in 2021/5/28 9:35
 */
@Data
public class SmsSignDTO {

    /**
     * 签名
     */
    private String signName;

    /**
     * 租户ID
     */
    private Integer tenantId;
}
