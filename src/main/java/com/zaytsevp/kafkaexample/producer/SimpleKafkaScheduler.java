package com.zaytsevp.kafkaexample.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Pavel Zaytsev
 */
@Component
public class SimpleKafkaScheduler {

    private final KafkaSender kafkaSender;

    @Autowired
    public SimpleKafkaScheduler(KafkaSender kafkaSender) {this.kafkaSender = kafkaSender;}

    @Scheduled(cron = "*/2 * * * * ?")
    public void send() {
        kafkaSender.send();
    }
}
