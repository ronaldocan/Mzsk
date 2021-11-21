package io.github.majusko.pulsar.demo;

import io.github.majusko.pulsar.annotation.PulsarConsumer;
import io.github.majusko.pulsar.constant.Serialization;
import io.github.majusko.pulsar.producer.PulsarTemplate;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TestConsumer {

    @Autowired
    private PulsarTemplate<String> producer;

    @PulsarConsumer(topic = "topic_for_normal", clazz = String.class, serialization = Serialization.JSON)
    public void topicForNormalListener(String myMsg) {
        System.out.println(String.format("normal:%s",myMsg));
    }

    @PulsarConsumer(topic = "topic_for_delay", clazz = String.class, serialization = Serialization.JSON)
    public void topicForDelayListener(String myMsg) throws PulsarClientException {

        System.out.println(String.format("delay:%s",myMsg));
        producer.sendDelay("topic_for_delay", 10L, TimeUnit.MINUTES,"延迟消息内容444444444来啦~");
    }

    @PulsarConsumer(topic = "topic_for_error", clazz = String.class, serialization = Serialization.JSON)
    public void topicForErrorListener(String myMsg) {
        System.out.println(String.format("error:%s",myMsg));
    }
}
