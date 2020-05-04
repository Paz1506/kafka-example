package com.zaytsevp.kafkaexample.listener;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Простой листенер
 *
 * @author Pavel Zaytsev
 */
@Component
@ConditionalOnProperty(name = "kafka.consumer.simple.enable", havingValue = "true")
public class SimpleKafkaListener {

    // не будем указывать смещение явно (читаем последнее)
    @KafkaListener(topicPartitions = @TopicPartition(topic = "${kafka.topic.name1:kafka-example-1}",
                                                     partitions = {"0", "1"}))
    public void listen(@Payload String message,
                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                       @Header(KafkaHeaders.OFFSET) int offset) {
        System.out.println("Received Message: [ " + message +
                           " ] from partition: [ " + partition +
                           " ] and offset: [ " + offset + " ]");
    }
}
