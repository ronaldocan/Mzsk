package com.mxsk.push.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 阿里查询发送短信详情响应DTO
 * @author: zhengguican
 * create in 2021/5/20 16:31
 */
@Data
public class QuerySmsDetailResponseDTO extends SmsResponseDTO {
    private static final long serialVersionUID = -5285518184741685151L;
    @JsonProperty("TotalCount")
    private Integer totalCount;
    @JsonProperty("SmsSendDetailDTOs")
    private SmsSendDetailDTOS smsSendDetailDTOs;

    @Data
    public static class SmsSendDetailDTOS implements Serializable {
        private static final long serialVersionUID = -7436206576324740687L;

        @JsonProperty("SmsSendDetailDTO")
        private List<SmsSendDetailDTO> smsSendDetailDTO;
    }

    @Data
    public static class SmsSendDetailDTO implements Serializable {
        private static final long serialVersionUID = 8962256214058101174L;

        @JsonProperty("TemplateCode")
        private String templateCode;
        @JsonProperty("ReceiveDate")
        private String receiveDate;
        @JsonProperty("PhoneNum")
        private String phoneNum;
        @JsonProperty("Content")
        private String content;
        @JsonProperty("SendStatus")
        private Integer sendStatus;
        @JsonProperty("ErrCode")
        private String errorCode;
        @JsonProperty("SendDate")
        private String sendDate;
    }
}
