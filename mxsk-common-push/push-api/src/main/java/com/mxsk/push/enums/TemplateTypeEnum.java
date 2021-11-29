package com.mxsk.push.enums;

/**
 * 短信模板类型枚举
 *
 * @author: zhengguican create in 2021/5/20 14:00
 */
public enum TemplateTypeEnum {
  REGISTER("1", "验证码"),
  NOTIFY("2", "推广短信"),
  INTERNATIONAL("3", "国际/港澳台消息");

  private String type;

  private String desc;

  TemplateTypeEnum(String type, String desc) {
    this.type = type;
    this.desc = desc;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }
}
