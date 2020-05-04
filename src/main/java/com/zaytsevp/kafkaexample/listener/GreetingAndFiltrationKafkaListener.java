package com.zaytsevp.kafkaexample.listener;

import com.zaytsevp.kafkaexample.model.Greeting;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Простой листенер с поддержкой фильтрации сообщений принимающий объекты Greeting
 *
 * @author Pavel Zaytsev
 */
@Component
@ConditionalOnProperty(name = {"payload.greeting.enabled", "kafka.consumer.greeting.enable"}, havingValue = "true")
public class GreetingAndFiltrationKafkaListener {

    // не будем указывать смещение явно (читаем последнее)
    @KafkaListener(topicPartitions = @TopicPartition(topic = "${kafka.topic.greeting}",
                                                     partitions = {"0", "1"}),
                   containerFactory = "kafkaListenerContainerFactoryWithGreetingFiltration")
    public void listen(@Payload Greeting message,
                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                       @Header(KafkaHeaders.OFFSET) int offset) {
        System.out.println("(Filtered) Received Message name: [ " + message.getName() +
                           " ] and payload: [ " + message.getMessage() +
                           " ] from partition: [ " + partition +
                           " ] and offset: [ " + offset + " ]");
    }
}
