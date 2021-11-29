package com.mxsk.push.enums;

/** @author: zhengguican create in 2021/5/20 13:53 */
public enum ResponseCodeEnum {
  SUCCESS(200, "请求成功"),
  FAIL(402, "请求失败"),
  ERROR(500, "请求异常");

  private Integer code;

  private String msg;

  ResponseCodeEnum(Integer code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}
