package com.mxsk.push.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhengguican
 * @description 接口返回code枚举
 * @date 2021/11/24 10:22
 */
@AllArgsConstructor
@Getter
public enum ResultCodeEnum {
  SUCCESS(200, "成功"),
  FAIL(402, "失败");

  private Integer code;
  private String msg;
}
