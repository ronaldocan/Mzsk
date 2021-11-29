package com.mxsk.push.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 接口响应状态码枚举
 *
 * @author: zhengguican
 * create in 2021/5/21 10:02
 */
@AllArgsConstructor
@Getter
public enum ResultCodeEnum {

    SUCCESS(Integer.valueOf(200)),
    FAIL(Integer.valueOf(402)),
    ERROR(Integer.valueOf(500));

    private Integer code;
}
