package com.mxsk.push.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: zhengguican
 * create in 2021/5/21 17:34
 */
@AllArgsConstructor
@Getter
public enum SignAuditStatusEnum {

    WAIT_AUDIT("0", "审核中"),
    AUDIT_PASS("1", "审核通过"),
    AUDIT_NOT_PASS("2", "审核失败");

    private String code;

    private String desc;
}
