package com.mxsk.push.config.provider;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 驱动配置类
 *
 * @author: zhengguican
 * create in 2021/5/12 14:22
 */
@Getter
@Setter
@ConfigurationProperties("sms.account")
public class SmsDriverMetadataProperties {

    /**
     * 源数据更新周期
     */
    private int metaFetchIntervalSeconds = 10;

    /**
     * 源数据缓存时间
     */
    private int metaExpireSeconds = 90;
}
