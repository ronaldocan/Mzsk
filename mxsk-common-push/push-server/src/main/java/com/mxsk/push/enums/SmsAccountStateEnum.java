package com.mxsk.push.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Sms短信平台账号状态枚举类
 * @author: zhengguican
 * create in 2021/5/21 14:05
 */
@AllArgsConstructor
@Getter
public enum SmsAccountStateEnum {

    ENABLE("enable", "启用"),
    DISABLE("disable", "禁用"),
    DELETE("delete", "作废删除");

    private String status;

    private String desc;
}
