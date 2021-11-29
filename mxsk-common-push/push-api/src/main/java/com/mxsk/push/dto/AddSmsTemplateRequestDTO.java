package com.mxsk.push.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 添加短信模板请求DTO
 *
 * @author: zhengguican create in 2021/5/20 13:58
 */
@Data
public class AddSmsTemplateRequestDTO {

  /** 租户ID */
  @ApiModelProperty("租户ID")
  private Long tenantId;

  /** 模板名称 */
  @ApiModelProperty("模板名称")
  private String templateName;

  /** 模板内容 */
  @ApiModelProperty("模板内容")
  private String templateContent;

  /** 模板类型 */
  @ApiModelProperty("模板类型")
  private String templateType;

  /** 备注 */
  @ApiModelProperty("备注")
  private String remark;

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Long getTenantId() {
    return tenantId;
  }

  public void setTenantId(Long tenantId) {
    this.tenantId = tenantId;
  }

  public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public String getTemplateContent() {
    return templateContent;
  }

  public void setTemplateContent(String templateContent) {
    this.templateContent = templateContent;
  }

  public String getTemplateType() {
    return templateType;
  }

  public void setTemplateType(String templateType) {
    this.templateType = templateType;
  }
}
