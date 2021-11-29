package com.mxsk.push.command.spi.aliyun;


import com.mxsk.push.command.Endpoint;
import com.mxsk.push.command.SmsCommandContext;

/**
 * @author zhengguican
 * create-at 2021/2/7
 */
public class AliyunSmsCommandContext implements SmsCommandContext {

    private String accessKey;

    private String secret;

    private String endpointId;

    public AliyunSmsCommandContext(String accessKey, String secret, String endpointId) {
        this.accessKey = accessKey;
        this.secret = secret;
        this.endpointId = endpointId;
    }

    @Override
    public String getAccessKey() {
        return accessKey;
    }

    @Override
    public String getSecret() {
        return secret;
    }

    @Override
    public Endpoint getEndpoint() {
        return Endpoint.of(this.endpointId);
    }
}
