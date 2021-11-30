package com.mxsk.push.mq;

import cn.hutool.json.JSONUtil;
import com.mxsk.push.config.mq.PulsarTopics;
import com.mxsk.push.dto.SendBatchSmsRequestDTO;
import com.mxsk.push.dto.SendSmsRequestDTO;
import com.mxsk.push.service.SmsPlatformService;
import io.github.majusko.pulsar.annotation.PulsarConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.SubscriptionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 短信系统相关mq消费者
 * @author: zhengguican
 * create in 2021/5/27 10:37
 */
@Slf4j
@Component
public class SmsTaskConsumer {

    @Autowired
    private SmsPlatformService smsPlatformService;

    /**
     * 发送短信任务队列
     * @param message
     */
    @PulsarConsumer(topic = PulsarTopics.SEND_SMS_OUTPUT, clazz = String.class, subscriptionType = SubscriptionType.Failover)
    public void sendSmsTask(String message) {
        SendSmsRequestDTO sendSmsRequestDTO = JSONUtil.toBean(message, SendSmsRequestDTO.class);
        smsPlatformService.saveSms(sendSmsRequestDTO);
    }

    /**
     * 批量发送短信任务队列
     * @param message
     */
    @PulsarConsumer(topic = PulsarTopics.SEND_BATCH_SMS_OUTPUT, clazz = String.class, subscriptionType = SubscriptionType.Failover)
    public void sendBatchSmsTask(String message) {
        SendBatchSmsRequestDTO sendBatchSmsRequestDTO = JSONUtil.toBean(message, SendBatchSmsRequestDTO.class);
        smsPlatformService.saveBatchSms(sendBatchSmsRequestDTO);
    }

    /**
     * 查询短信发送状态任务队列
     * @param message
     */
    @PulsarConsumer(topic = PulsarTopics.QUERY_SMS_SEND_STATUS, clazz = String.class, subscriptionType = SubscriptionType.Failover)
    public void smsDetailQueryTask(String message) {
        this.smsPlatformService.updateSmsSendStatus(message);
    }
}
