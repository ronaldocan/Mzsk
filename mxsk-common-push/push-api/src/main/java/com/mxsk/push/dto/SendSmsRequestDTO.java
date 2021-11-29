package com.mxsk.push.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

/**
 * 短信请求DTO
 *
 * @author: zhengguican create in 2021/5/20 10:13
 */
@ApiModel(description = "发送短信请求DTO")
@Data
@Builder
public class SendSmsRequestDTO {

  /** 租户ID */
  @ApiModelProperty("租户ID")
  private Long tenantId;

  /** 模板编号 */
  @ApiModelProperty("短信模板编号")
  private String templateCode;

  /** 手机号码 */
  @ApiModelProperty("手机号码")
  private String phoneNumber;

  /** 短信签名 */
  @ApiModelProperty("短信")
  private String signName;

  /** 短信模板参数 */
  @ApiModelProperty("短信模板参数")
  private Map<String, String> paramMap;
}
