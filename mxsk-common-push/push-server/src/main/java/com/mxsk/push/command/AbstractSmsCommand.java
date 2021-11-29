package com.mxsk.push.command;


import com.mxsk.push.cache.AccessInfoMeta;

/**
 * @author zhengguican
 * create-at 2021/2/7
 */
public abstract class AbstractSmsCommand implements SmsCommand {

    protected SmsClient smsClient;

    protected AccessInfoMeta accessInfoMeta;

    public SmsClient getSmsClient() {
        return smsClient;
    }

    public void setSmsClient(SmsClient smsClient) {
        this.smsClient = smsClient;
    }

    public AccessInfoMeta getAccessInfoMeta() {
        return accessInfoMeta;
    }

    public void setAccessInfoMeta(AccessInfoMeta accessInfoMeta) {
        this.accessInfoMeta = accessInfoMeta;
    }
}
