package com.mxsk.push.command.spi.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.IClientProfile;
import com.mxsk.push.command.SmsClient;

/**
 * 阿里云短信SDK Client 适配类
 *
 * @author: zhengguican
 * create in 2021/5/20 11:23
 */
public class AliyunSmsClientAdapter extends DefaultAcsClient implements SmsClient {

    public AliyunSmsClientAdapter(IClientProfile profile) {
        super(profile);
    }

    @Override
    public void closeClient() {
        super.shutdown();
    }
}
