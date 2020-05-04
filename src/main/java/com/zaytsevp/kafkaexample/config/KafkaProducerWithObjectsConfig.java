package com.zaytsevp.kafkaexample.config;

import com.zaytsevp.kafkaexample.model.Greeting;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурация продюсеров
 * <p>
 * Настраиваем сериализацию для кастомных Java-объектов
 *
 * @author Pavel Zaytsev
 */
@Configuration
@ConditionalOnProperty(name = "payload.greeting.enabled", havingValue = "true")
public class KafkaProducerWithObjectsConfig {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    /**
     * фабрика продюсеров, для задания параметров создания их экземпляров
     * Ключ - String
     * Значение - Greeting
     */
    @Bean
    public ProducerFactory<String, Greeting> greetingProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();

        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Greeting> kafkaTemplate(ProducerFactory<String, Greeting> greetingProducerFactory) {
        return new KafkaTemplate<>(greetingProducerFactory);
    }
}
