package com.mxsk.push.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * 添加短信签名请求DTO
 *
 * @author: zhengguican create in 2021/5/20 13:58
 */
@Data
@Builder
public class AddSmsSignRequestDTO {

  /** 租户ID */
  @ApiModelProperty("租户ID")
  private Long tenantId;

  /** 签名名称 */
  @ApiModelProperty("签名名称")
  private String signName;

  /** 签名来源 */
  @ApiModelProperty("签名来源")
  private String signSource;

  /** 模板类型 */
  @ApiModelProperty("remark")
  private String remark;

  /** 备注 */
  @ApiModelProperty("签名图片")
  private List<SignFileDTO> signFileList;
}
