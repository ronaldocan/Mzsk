package com.mxsk.push.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

/**
 * 短信模板接口请求DTO
 *
 * @author: zhengguican create in 2021/5/21 14:16
 */
@Data
@ApiModel(description = "短信模板查询结果DTO")
public class SmsTemplateQueryResponseDTO {

  /** 模板类型 */
  @ApiModelProperty("模板类型")
  private String templateType;

  /** 模板内容 */
  @ApiModelProperty("模板内容")
  private String templateContent;

  /** 创建时间 */
  @ApiModelProperty("创建时间")
  private Date createDate;

  /** 模板审核状态 */
  @ApiModelProperty("模板审核状态")
  private String templateStatus;
}
