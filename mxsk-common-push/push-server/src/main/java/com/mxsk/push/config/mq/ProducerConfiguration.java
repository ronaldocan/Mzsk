package com.mxsk.push.config.mq;

import io.github.majusko.pulsar.producer.ProducerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Date 2021/11/24 15:53
 * @Author zhengguican
 */
@Configuration
public class ProducerConfiguration {

    @Bean
    public ProducerFactory producerFactory() {
        return new ProducerFactory()
                .addProducer(PulsarTopics.QUERY_SMS_SEND_STATUS, String.class);
    }
}
