package com.mxsk.push.command;


import com.mxsk.push.dto.Result;
import com.mxsk.push.exception.SmsCommandException;


/**
 * 短信平台请求命令
 *
 * @author zhengguican
 * create-at 2021/2/7
 */
public interface SmsCommand {

    /**
     * 执行命令
     *
     * @return
     * @throws SmsCommandException
     */
    Result execute() throws SmsCommandException;
}
