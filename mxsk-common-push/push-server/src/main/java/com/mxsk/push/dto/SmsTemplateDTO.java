package com.mxsk.push.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 短信模板记录DTO
 * @author: zhengguican
 * create in 2021/5/26 17:08
 */
@Data
public class SmsTemplateDTO {

    @ApiModelProperty("租户ID")
    private Integer tenantId;

    @ApiModelProperty("模板编码")
    private String templateCode;

    @ApiModelProperty("短信平台账号ID")
    private Integer accountId;
}
