package com.mxsk.push.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 阿里云短信接口响应dto
 * @author: zhengguican
 * create in 2021/5/20 15:02
 */
@Data
public class SmsResponseDTO {

    /**
     * 响应编码
     */
    @JsonProperty("Code")
    private String Code;

    /**
     * 响应消息
     */
    @JsonProperty("Message")
    private String Message;

    /**
     * 请求序列号
     */
    @JsonProperty("RequestId")
    private String RequestId;

    /**
     * 业务编码
     */
    @JsonProperty("BizId")
    private String BizId;
}
