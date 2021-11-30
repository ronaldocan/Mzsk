package com.mxsk.push.config.provider;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mxsk.push.cache.AccessInfoCache;
import com.mxsk.push.cache.MetaCache;
import com.mxsk.push.mapper.SmsAccountMapper;
import lombok.AllArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * Sms驱动配置类
 * @author: zhengguican
 * create in 2021/5/12 14:10
 */
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(SmsDriverMetadataProperties.class)
@MapperScan("com.mxsk.push.mapper")
public class SmsDriverConfig {

    @Bean
    public MetadataProvider metadataProvider(SmsAccountMapper smsAccountMapper) {
        return new AccessTokenProvider(smsAccountMapper);
    }

    @Bean
    public MetaCache metaCache(SmsDriverMetadataProperties smsDriverMetadataProperties,
                               MetadataProvider metadataProvider) {
        return new AccessInfoCache(smsDriverMetadataProperties, (AccessTokenProvider) metadataProvider);
    }

    @Bean
    public ScheduledThreadPoolExecutor smsTaskExecutor() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("query-status-%d").build();
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(4,namedThreadFactory);
        return executor;
    }

}
