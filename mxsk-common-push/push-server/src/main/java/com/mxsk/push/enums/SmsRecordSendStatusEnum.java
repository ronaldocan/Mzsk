package com.mxsk.push.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 短信发送状态枚举
 * @author: zhengguican
 * create in 2021/5/21 17:34
 */
@AllArgsConstructor
@Getter
public enum SmsRecordSendStatusEnum {

    WAIT_SEND("1", "待发送"),
    SUCCESS("2", "发送成功"),
    FAIL("3", "发送失败");

    private String code;

    private String desc;
}
