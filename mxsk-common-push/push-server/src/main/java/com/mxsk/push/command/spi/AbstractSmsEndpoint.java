package com.mxsk.push.command.spi;


import com.mxsk.push.command.SmsCommandContext;
import com.mxsk.push.command.SmsEndpoint;

/**
 * Sms命令抽象类
 *
 * @author zhengguican
 * create-at 2021/2/7
 */
public abstract class AbstractSmsEndpoint<T> implements SmsEndpoint<T> {

    protected SmsCommandContext smsCommandContext;

    public AbstractSmsEndpoint(SmsCommandContext smsCommandContext) {
        this.smsCommandContext = smsCommandContext;
    }

    @Override
    public String getEndpointUrl() {
        return smsCommandContext.getEndpoint().getUrl();
    }

}
