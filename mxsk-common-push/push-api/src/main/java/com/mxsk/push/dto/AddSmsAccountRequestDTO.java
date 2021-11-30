package com.mxsk.push.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 添加短信帐号记录请求DTO
 *
 * @author: zhengguican create in 2021/5/20 10:13
 */
@ApiModel(description = "添加短信帐号记录请求DTO")
@Data
public class AddSmsAccountRequestDTO {

  @ApiModelProperty("租户ID")
  private Long tenantId;

  @ApiModelProperty("区号")
  private String areaCode;

  @ApiModelProperty("短信平台账号KEY")
  private String accessKey;

  @ApiModelProperty("短信平台账号密钥")
  private String secret;

  @ApiModelProperty("短信平台类型")
  private String platform;

  @ApiModelProperty("服务地址")
  private String endPoint;
}
