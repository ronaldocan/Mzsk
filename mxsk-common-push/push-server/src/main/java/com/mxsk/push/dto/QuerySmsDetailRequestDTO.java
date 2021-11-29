package com.mxsk.push.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;


/**
 * 查询短信详情请求DTO
 * @author: zhengguican
 * create in 2021/5/20 10:13
 */
@ApiModel(description = "查询短信详情请求DTO")
@Data
@Builder
public class QuerySmsDetailRequestDTO {

    /**
     * 租户ID
     */
    @ApiModelProperty("租户ID")
    private Long tenantId;

    /**
     * 手机号码
     */
    @ApiModelProperty("手机号码")
    private String phoneNumber;

    /**
     * 手机号码
     */
    @ApiModelProperty("请求序列号")
    private String requestId;

    /**
     * 发送日期
     */
    @ApiModelProperty("发送日期")
    private String sendDate;
}
