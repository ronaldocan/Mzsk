package com.mxsk.push.command;


/**
 * Sms客户端环境上下文
 *
 * @author zhengguican
 * create-at 2021/2/7
 */
public interface SmsCommandContext {

    /**
     * 获取访问key
     *
     * @return
     */
    String getAccessKey();

    /**
     * 获取密钥
     *
     * @return
     */
    String getSecret();

    /**
     * 获取接口endpoint
     *
     * @return
     */
    Endpoint getEndpoint();
}
