package com.mxsk.push.command;

/**
 * 短信请求终端
 *
 * @author zhengguican
 * create-at 2021/2/7
 */
public interface SmsEndpoint<T> {

    /**
     * 获取地域url
     *
     * @return
     */
    String getEndpointUrl();

    /**
     * 开启SMS client
     *
     * @return
     */
    T openClient();
}
