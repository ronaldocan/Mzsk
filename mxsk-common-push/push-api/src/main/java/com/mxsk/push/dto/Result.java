package com.mxsk.push.dto;

import lombok.Data;

/** @Description 接口返回常量类 @Author zhengguican @Date 2021/11/24 10:21 */
@Data
public class Result<T> {

  private Integer code;
  private String message;
  private T data;

  public Result() {}

  public Result(Integer code, String msg) {
    this.code = code;
    this.setMessage(msg);
  }

  public Result(Integer code, String msg, T data) {
    this.code = code;
    this.data = data;
    this.setMessage(msg);
  }

  public static Result success() {
    return new Result<>(200, "成功");
  }

  public static <T> Result success(T t) {
    return new Result<>(200, "成功", t);
  }

  public static Result fail(String msg) {
    return new Result<>(402, msg);
  }

  public static Result error(Integer code, String msg) {
    return new Result<>(code, msg);
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
