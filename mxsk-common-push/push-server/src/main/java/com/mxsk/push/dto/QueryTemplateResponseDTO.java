package com.mxsk.push.dto;

import lombok.Data;

import java.util.Date;

/**
 * 阿里查询短信模板接口响应DTO
 * @author: zhengguican
 * create in 2021/5/20 16:31
 */
@Data
public class QueryTemplateResponseDTO extends SmsResponseDTO {

    /**
     * 模板类型
     */
    private String TemplateType;

    /**
     * 模板内容
     */
    private String TemplateContent;

    /**
     * 创建时间
     */
    private Date CreateDate;

    /**
     * 模板审核状态
     */
    private String TemplateStatus;
}
