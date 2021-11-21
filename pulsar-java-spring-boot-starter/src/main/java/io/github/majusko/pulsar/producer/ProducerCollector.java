package io.github.majusko.pulsar.producer;

import io.github.majusko.pulsar.collector.ProducerHolder;
import io.github.majusko.pulsar.error.exception.ProducerInitException;
import io.github.majusko.pulsar.utils.SchemaUtils;
import io.github.majusko.pulsar.utils.UrlBuildService;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class ProducerCollector implements EmbeddedValueResolverAware{

    private final PulsarClient pulsarClient;
    private final UrlBuildService urlBuildService;
    private final ProducerFactory producerFactory;

    private final Map<String, Producer> producers = new ConcurrentHashMap<>();

    private StringValueResolver stringValueResolver;

    public ProducerCollector(PulsarClient pulsarClient, UrlBuildService urlBuildService, ProducerFactory producerFactory) {
        this.pulsarClient = pulsarClient;
        this.urlBuildService = urlBuildService;
        this.producerFactory = producerFactory;
    }

    @PostConstruct
    public void setProducer() {
        if (!producerFactory.getTopics().isEmpty()) {
            producers.putAll((producerFactory).getTopics().entrySet().stream()
                    .map($ -> new ProducerHolder(
                            stringValueResolver.resolveStringValue($.getKey()),
                            $.getValue().left,
                            $.getValue().right))
                    .collect(Collectors.toMap(ProducerHolder::getTopic, this::buildProducer)));
        }
    }

    private Producer<?> buildProducer(ProducerHolder holder) {
        try {
            return pulsarClient.newProducer(getSchema(holder))
                .topic(urlBuildService.buildTopicUrl(holder.getTopic()))
                .create();
        } catch (PulsarClientException e) {
            throw new ProducerInitException("Failed to init producer.", e);
        }
    }

    private <T> Schema<?> getSchema(ProducerHolder holder) throws RuntimeException {
        return SchemaUtils.getSchema(holder.getSerialization(), holder.getClazz());
    }

    public Producer getProducer(String topic) {
        return producers.get(stringValueResolver.resolveStringValue(topic));
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver stringValueResolver) {
        this.stringValueResolver = stringValueResolver;
    }
}
