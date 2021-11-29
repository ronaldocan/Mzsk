package com.mxsk.push.command;

import com.mxsk.push.cache.AccessInfoMeta;
import com.mxsk.push.command.spi.aliyun.AliyunSmsCommandContext;
import com.mxsk.push.command.spi.aliyun.AliyunSmsEndpoint;
import com.mxsk.push.dto.Result;
import com.mxsk.push.enums.SmsPlatformEnum;
import com.mxsk.push.exception.SmsCommandException;
import org.springframework.stereotype.Component;


/**
 * @author zhengguican
 * create-at 2021/2/7
 */
@Component
public class SmsEngine {

    /**
     * 执行短信相关命令
     *
     * @param accessInfoMeta 短信平台配置元数据
     * @param smsCommand     短信命令
     * @return
     */
    public Result process(AccessInfoMeta accessInfoMeta, SmsCommand smsCommand) {
        SmsClient smsClient = null;
        SmsPlatformEnum smsPlatformEnum = SmsPlatformEnum.of(accessInfoMeta.getPlatform());
        switch (smsPlatformEnum) {
            case ALIYUN:
                SmsCommandContext ctx = new AliyunSmsCommandContext(accessInfoMeta.getAccessKey(),
                        accessInfoMeta.getSecret(), accessInfoMeta.getEndpoint());
                AliyunSmsEndpoint endpoint = new AliyunSmsEndpoint(ctx);
                smsClient = endpoint.openClient();
                break;
            case TENCENT:
                break;
            default:
                break;
        }
        if (smsCommand instanceof AbstractSmsCommand) {
            ((AbstractSmsCommand) smsCommand).setSmsClient(smsClient);
        }
        try {
            return smsCommand.execute();
        } catch (SmsCommandException e) {
            throw e;
        } finally {
            smsClient.closeClient();
        }
    }
}
