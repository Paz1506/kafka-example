package com.zaytsevp.kafkaexample.producer;

import com.zaytsevp.kafkaexample.model.Greeting;
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
@ConditionalOnProperty(name = "payload.greeting.enabled", havingValue = "true")
public class GreetingKafkaSender implements KafkaSender {

    private final KafkaTemplate<String, Greeting> kafkaTemplate;

    @Value("${payload.string.length:10}")
    private int payloadStringLength;

    @Value("${kafka.topic.greeting}")
    private String topicName;

    @Autowired
    public GreetingKafkaSender(KafkaTemplate<String, Greeting> kafkaTemplate) {this.kafkaTemplate = kafkaTemplate;}

    /**
     * асинхронная отправка, без блокироующего ожидания результата отправки
     */
    public void send() {
        String messageName = StringGenerator.generateRandomString(3);

        ListenableFuture<SendResult<String, Greeting>> future =
                kafkaTemplate.send(topicName, new Greeting(StringGenerator.generateRandomStringWithRandomWorld(payloadStringLength),
                                                           "name-" +
                                                           messageName));

        // callback - для асинхронного ожидания результата отправки и вывода его в консоль
        future.addCallback(new ListenableFutureCallback<SendResult<String, Greeting>>() {

            @Override
            public void onSuccess(SendResult<String, Greeting> result) {
                System.out.println("Sent message=[" + messageName +
                                   "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=["
                                   + messageName + "] due to : " + ex.getMessage());
            }
        });
    }
}
