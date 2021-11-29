package com.mxsk.push.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 短信接入平台枚举
 * @author: zhengguican
 * create in 2021/5/20 11:28
 */
@AllArgsConstructor
@Getter
public enum SmsPlatformEnum {

    ALIYUN("aliyun", "阿里云"),
    TENCENT("tencent", "腾讯云");

    private String code;

    private String desc;

    public static SmsPlatformEnum of(String code) {
        for (SmsPlatformEnum smsPlatformEnum : SmsPlatformEnum.values()) {
            if (smsPlatformEnum.code.equals(code)) {
                return smsPlatformEnum;
            }
        }
        return null;
    }
}
