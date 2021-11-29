package com.mxsk.push.dto;

import lombok.Data;

/**
 * 添加短信签名请求响应记录dto
 * @author: zhengguican
 * create in 2021/5/21 15:29
 */
@Data
public class AddSmsTemplateResponseDTO extends SmsResponseDTO {

    /**
     * 模板编号
     */
    private String TemplateCode;

    /**
     * 响应内容
     */
    private String msg;
}
