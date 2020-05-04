package com.zaytsevp.kafkaexample.config;

import com.zaytsevp.kafkaexample.model.Greeting;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурация консьюмеров для кастомного объекта Greeting
 * <p>
 * Обязательно необходимо сконфигурировать ConsumerFactory и KafkaListenerContainerFactory
 *
 * @author Pavel Zaytsev
 */
@Configuration
@EnableKafka
@ConditionalOnProperty(name = "payload.greeting.enabled", havingValue = "true")
public class KafkaConsumerWithObjectsConfig {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${kafka.group.1.id}")
    private String group1Id;

    @Bean
    public ConsumerFactory<String, Greeting> greetingConsumerFactory() {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, group1Id);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest"); // хотим последние сообщения

        // https://stackoverflow.com/questions/51688924/spring-kafka-the-class-is-not-in-the-trusted-packages
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(props);
    }

    /**
     * простейший контейнер листереров с поддержкой Json-десериализации объектов типа Greeting
     * и фильтрацией сообщений по содержимому
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Greeting> kafkaListenerContainerFactoryWithGreetingFiltration(ConsumerFactory<String, Greeting> greetingConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, Greeting> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(greetingConsumerFactory);

        // Фильтр для сообщений - которые НЕ будут обрабатываться
        factory.setRecordFilterStrategy(record -> !record.value()
                                                         .getMessage()
                                                         .contains("world"));

        return factory;
    }
}
