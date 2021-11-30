package com.mxsk.push.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * 批量发送短信请求DTO
 *
 * @author: zhengguican create in 2021/5/20 10:13
 */
@ApiModel(description = "批量发送短信请求DTO")
@Data
public class SendBatchSmsRequestDTO {

  /** 租户ID */
  @ApiModelProperty("租户ID")
  private Long tenantId;

  /** 模板编号 */
  @ApiModelProperty("短信模板编号")
  private String templateCode;

  /** 手机号码 */
  @ApiModelProperty("手机号码集合")
  private List<String> phoneList;

  /** 短信签名 */
  @ApiModelProperty("短信签名")
  private List<String> signNameList;

  /** 短信模板参数 */
  @ApiModelProperty("短信模板参数")
  private List<Map<String, String>> paramList;
}
