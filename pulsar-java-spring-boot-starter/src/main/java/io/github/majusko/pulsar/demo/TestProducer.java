package io.github.majusko.pulsar.demo;

import io.github.majusko.pulsar.msg.MyMsg;
import io.github.majusko.pulsar.producer.PulsarTemplate;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class TestProducer {

    @Autowired
    private PulsarTemplate<String> producer;

    @PostConstruct
    public void testProduce() throws PulsarClientException {
        producer.send("topic_for_normal", "接受正常消息~");
        producer.send("topic_for_error", "接受错误消息~");
        producer.sendDelay("topic_for_delay", 10L,TimeUnit.SECONDS,"延迟消息内容444444444来啦~");
    }


}
