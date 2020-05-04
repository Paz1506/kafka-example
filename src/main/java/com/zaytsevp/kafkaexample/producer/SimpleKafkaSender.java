package com.zaytsevp.kafkaexample.producer;

import com.zaytsevp.kafkaexample.util.StringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * Простой асинхронный отправитель
 *
 * @author Pavel Zaytsev
 */
@Component
@ConditionalOnProperty(name = "payload.string.enabled", havingValue = "true")
public class SimpleKafkaSender implements KafkaSender {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${payload.string.length:10}")
    private int payloadStringLength;

    @Value("${kafka.topic.name1:kafka-example-0}")
    private String topicName;

    @Autowired
    public SimpleKafkaSender(KafkaTemplate<String, String> kafkaTemplate) {this.kafkaTemplate = kafkaTemplate;}

    /**
     * асинхронная отправка, без блокироующего ожидания результата отправки
     */
    public void send() {
        String message = StringGenerator.generateRandomString(payloadStringLength);

        ListenableFuture<SendResult<String, String>> future =
                kafkaTemplate.send(topicName, StringGenerator.generateRandomStringWithRandomWorld(payloadStringLength));

        // callback - для асинхронного ожидания результата отправки и вывода его в консоль
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("Sent message=[" + message +
                                   "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=["
                                   + message + "] due to : " + ex.getMessage());
            }
        });
    }
}
