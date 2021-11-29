package com.mxsk.push.exception;

/**
 * 短信操作命令异常
 *
 * @author: zhengguican
 * create in 2021/5/19 10:46
 */
public class SmsCommandException extends RuntimeException {

    public SmsCommandException(ErrorCode code, String errMsg) {
        super(errMsg);
    }
}
