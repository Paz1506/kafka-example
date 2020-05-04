package com.zaytsevp.kafkaexample.listener;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Простой листенер, который вычитывает определенные партиции топика
 *
 * @author Pavel Zaytsev
 */
@Component
@ConditionalOnProperty(name = "kafka.consumer.withpartiotions.enable", havingValue = "true")
public class SimpleKafkaListenerWithPartitions {

    @KafkaListener(topics = {"kafka-example-0", "${kafka.topic.name1:kafka-example-1}"},
                   topicPartitions = { // Указываем явно, с каким смещением читаем сообщения
                                       @TopicPartition(
                                               topic = "${kafka.topic.name1:kafka-example-1}",
                                               partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0")
                                       )
                   },
                   groupId = "${kafka.group.1.id:group1}")
    public void listenFromPartition(@Payload String message,
                                    // поддерживает извлечение информации из хэдера
                                    @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                    @Header(KafkaHeaders.OFFSET) int offset) {
        System.out.println("Received Message: [ " + message +
                           " ] from partition: [ " + partition +
                           " ] and offset: [ " + offset + " ]");
    }
}
