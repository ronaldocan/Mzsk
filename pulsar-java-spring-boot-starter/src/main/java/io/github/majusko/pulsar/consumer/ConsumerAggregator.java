package io.github.majusko.pulsar.consumer;

import com.google.common.base.Strings;
import io.github.majusko.pulsar.PulsarMessage;
import io.github.majusko.pulsar.collector.ConsumerCollector;
import io.github.majusko.pulsar.collector.ConsumerHolder;
import io.github.majusko.pulsar.error.FailedMessage;
import io.github.majusko.pulsar.error.exception.ClientInitException;
import io.github.majusko.pulsar.error.exception.ConsumerInitException;
import io.github.majusko.pulsar.properties.ConsumerProperties;
import io.github.majusko.pulsar.utils.SchemaUtils;
import io.github.majusko.pulsar.utils.UrlBuildService;
import org.apache.pulsar.client.api.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;
import reactor.core.Disposable;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@DependsOn({"pulsarClient", "consumerCollector"})
public class ConsumerAggregator implements EmbeddedValueResolverAware {

    private final Sinks.Many<FailedMessage> sink = Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
    private final ConsumerCollector consumerCollector;
    private final PulsarClient pulsarClient;
    private final ConsumerProperties consumerProperties;
    private final UrlBuildService urlBuildService;
    private final static SubscriptionType DEFAULT_SUBSCRIPTION_TYPE = SubscriptionType.Exclusive;

    private StringValueResolver stringValueResolver;
    private List<Consumer> consumers;

    public ConsumerAggregator(ConsumerCollector consumerCollector, PulsarClient pulsarClient,
                              ConsumerProperties consumerProperties, UrlBuildService urlBuildService) {
        this.consumerCollector = consumerCollector;
        this.pulsarClient = pulsarClient;
        this.consumerProperties = consumerProperties;
        this.urlBuildService = urlBuildService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        consumers = consumerCollector.getConsumers().entrySet().stream()
            .map(holder -> subscribe(holder.getKey(), holder.getValue()))
            .collect(Collectors.toList());
    }

    /**
     * 通过反射调用消费者订阅方法
     *
     * @param generatedConsumerName
     * @param holder
     * @return
     */
    private Consumer<?> subscribe(String generatedConsumerName, ConsumerHolder holder) {
        try {
            final String consumerName = stringValueResolver.resolveStringValue(holder.getAnnotation().consumerName());
            final String subscriptionName = stringValueResolver.resolveStringValue(holder.getAnnotation().subscriptionName());
            final String topicName = stringValueResolver.resolveStringValue(holder.getAnnotation().topic());
            final SubscriptionType subscriptionType = getSubscriptionType(holder);
            final ConsumerBuilder<?> consumerBuilder = pulsarClient
                .newConsumer(SchemaUtils.getSchema(holder.getAnnotation().serialization(),
                    holder.getAnnotation().clazz()))
                .consumerName(urlBuildService.buildPulsarConsumerName(consumerName, generatedConsumerName))
                .subscriptionName(urlBuildService.buildPulsarSubscriptionName(subscriptionName, generatedConsumerName))
                .topic(urlBuildService.buildTopicUrl(topicName))
                .subscriptionType(subscriptionType)
                .messageListener((consumer, msg) -> {
                    try {
                        final Method method = holder.getHandler();
                        method.setAccessible(true);
                        if (holder.isWrapped()) {
                            method.invoke(holder.getBean(), wrapMessage(msg));
                        } else {
                            method.invoke(holder.getBean(), msg.getValue());
                        }
                        consumer.acknowledge(msg);
                    } catch (Exception e) {
                        consumer.negativeAcknowledge(msg);
                        sink.tryEmitNext(new FailedMessage(e, consumer, msg));
                    }
                });
            if (consumerProperties.getAckTimeoutMs() > 0) {
                consumerBuilder.ackTimeout(consumerProperties.getAckTimeoutMs(), TimeUnit.MILLISECONDS);
            }

            buildDeadLetterPolicy(holder, consumerBuilder);
            return consumerBuilder.subscribe();
        } catch (PulsarClientException | ClientInitException e) {
            throw new ConsumerInitException("Failed to init consumer.", e);
        }
    }

    private SubscriptionType getSubscriptionType(ConsumerHolder holder) throws ClientInitException {
        SubscriptionType subscriptionType = Arrays.stream(holder.getAnnotation().subscriptionType())
                .findFirst().orElse(null);

        if (subscriptionType == null && Strings.isNullOrEmpty(consumerProperties.getSubscriptionType())) {
            subscriptionType = DEFAULT_SUBSCRIPTION_TYPE;
        } else if (subscriptionType == null && !Strings.isNullOrEmpty(consumerProperties.getSubscriptionType())) {
            try {
                subscriptionType = SubscriptionType.valueOf(consumerProperties.getSubscriptionType());
            } catch (IllegalArgumentException exception) {
                throw new ClientInitException("There was unknown SubscriptionType.", exception);
            }
        }

        return subscriptionType;
    }

    public void buildDeadLetterPolicy(ConsumerHolder holder, ConsumerBuilder<?> consumerBuilder) {
        DeadLetterPolicy.DeadLetterPolicyBuilder deadLetterBuilder = null;

        if (consumerProperties.getDeadLetterPolicyMaxRedeliverCount() >= 0) {
            deadLetterBuilder =
                DeadLetterPolicy.builder().maxRedeliverCount(consumerProperties.getDeadLetterPolicyMaxRedeliverCount());
        }

        if (holder.getAnnotation().maxRedeliverCount() >= 0) {
            deadLetterBuilder =
                DeadLetterPolicy.builder().maxRedeliverCount(holder.getAnnotation().maxRedeliverCount());
        }

        if (deadLetterBuilder != null && !holder.getAnnotation().deadLetterTopic().isEmpty()) {
            deadLetterBuilder.deadLetterTopic(urlBuildService.buildTopicUrl(holder.getAnnotation().deadLetterTopic()));
        }

        if (deadLetterBuilder != null) {
            consumerBuilder.deadLetterPolicy(deadLetterBuilder.build());
        }
    }

    public <T> PulsarMessage<T> wrapMessage(Message<T> message) {
        final PulsarMessage<T> pulsarMessage = new PulsarMessage<T>();

        pulsarMessage.setValue(message.getValue());
        pulsarMessage.setMessageId(message.getMessageId());
        pulsarMessage.setSequenceId(message.getSequenceId());
        pulsarMessage.setProperties(message.getProperties());
        pulsarMessage.setTopicName(message.getTopicName());
        pulsarMessage.setKey(message.getKey());
        pulsarMessage.setEventTime(message.getEventTime());
        pulsarMessage.setPublishTime(message.getPublishTime());
        pulsarMessage.setProducerName(message.getProducerName());

        return pulsarMessage;
    }

    public List<Consumer> getConsumers() {
        return consumers;
    }

    public Disposable onError(java.util.function.Consumer<? super FailedMessage> consumer) {
        return sink.asFlux().subscribe(consumer);
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver stringValueResolver) {
        this.stringValueResolver = stringValueResolver;
    }
}
