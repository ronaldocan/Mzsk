package com.mxsk.push.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 短信模板接口请求DTO
 *
 * @author: zhengguican create in 2021/5/21 14:16
 */
@Data
public class SmsTemplateQueryRequestDTO {

  @ApiModelProperty("租户ID")
  private Long tenantId;

  @ApiModelProperty("模板编码")
  private String templateCode;
}
