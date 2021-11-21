package io.github.majusko.pulsar;

import io.github.majusko.pulsar.demo.TestConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({TestConsumer.class})
public class PulsarJavaSpringBootStarterApplication {
    public static void main(String[] args) {
        SpringApplication.run(PulsarJavaSpringBootStarterApplication.class, args);
    }
}
