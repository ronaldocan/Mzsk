package com.mxsk.push.command.spi.aliyun;

import com.aliyuncs.profile.DefaultProfile;
import com.mxsk.push.command.SmsCommandContext;
import com.mxsk.push.command.spi.AbstractSmsEndpoint;

/**
 * @author zhengguican
 * create-at 2021/2/7
 */
public class AliyunSmsEndpoint extends AbstractSmsEndpoint<AliyunSmsClientAdapter> {

    public AliyunSmsEndpoint(SmsCommandContext smsCommandContext) {
        super(smsCommandContext);
    }

    @Override
    public AliyunSmsClientAdapter openClient() {
        DefaultProfile profile = DefaultProfile.getProfile(getEndpointUrl(), smsCommandContext.getAccessKey(), smsCommandContext.getSecret());
        AliyunSmsClientAdapter client = new AliyunSmsClientAdapter(profile);
        return client;
    }
}
