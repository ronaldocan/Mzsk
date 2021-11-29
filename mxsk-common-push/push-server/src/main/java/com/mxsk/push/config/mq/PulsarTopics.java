package com.mxsk.push.config.mq;

/**
 * @Description pulsar topic
 * @Date 2021/11/24 15:42
 * @Author zhengguican
 */
public class PulsarTopics {

    private PulsarTopics() {
    }

    /**
     * 查询短信发送状态
     */
    public static final String QUERY_SMS_SEND_STATUS = "query-sms-send-status";

    /**
     * 批量发送短信
     */
    public static final String SEND_BATCH_SMS_OUTPUT = "send-batch-sms-output";

    /**
     * 发送单条短信
     */
    public static final String SEND_SMS_OUTPUT = "send-sms-output";
}
