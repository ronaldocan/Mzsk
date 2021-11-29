package com.mxsk.push.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 短信签名来源
 *
 * @author: zhengguican create in 2021/5/21 14:37
 */
@Getter
@AllArgsConstructor
public enum SignSourceTypeEnum {
  ENTERPRISE("0", "企事业单位的全称或简称"),
  WEBSITE("1", "工信部备案网站的全称或简称"),
  APP("2", "APP应用的全称或简称"),
  MINA("3", "公众号或小程序的全称或简称"),
  E_COMMERCE("4", "电商平台店铺名的全称或简称"),
  BRAND_NAME("5", "商标名的全称或简称");

  private String code;

  private String desc;
}
