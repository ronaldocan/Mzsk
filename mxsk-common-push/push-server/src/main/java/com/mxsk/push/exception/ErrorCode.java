package com.mxsk.push.exception;

/**
 * @author: zhengguican
 * create in 2021/5/19 10:45
 */
public enum ErrorCode {

    /**
     * 短信发送错误
     */
    SMS_SEND_ERROR(1000),

    /**
     * 创建短信模板错误
     */
    CREATE_TEMPLATE_ERROR(1001),

    /**
     * 查询短信模板错误
     */
    QUERY_TEMPLATE_ERROR(1002),

    /**
     * 创建短信签名错误
     */
    CREATE_SIGN_ERROR(1003),

    /**
     * 查询短信签名模板错误
     */
    QUERY_SIGN_ERROR(1004),

    /**
     * 查询发送短信状态任务错误
     */
    QUERY_SMS_SEND_STATUS(1005),

    /**
     * 参数异常
     */
    INVALID_PARAMETERS(2),

    /**
     * unknow error.
     */
    UNKNOW_ERROR(40001);

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}