package com.mxsk.push.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 平台审核状态枚举
 * @author: zhengguican
 * create in 2021/5/21 17:34
 */
@AllArgsConstructor
@Getter
public enum TemplateAuditStatusEnum {

    WAIT_AUDIT("0", "审核中"),
    AUDIT_PASS("1", "审核通过"),
    AUDIT_NOTPASS("2", "审核失败");

    /**
     * 编码
     */
    private String code;

    /**
     * 描述
     */
    private String desc;
}
