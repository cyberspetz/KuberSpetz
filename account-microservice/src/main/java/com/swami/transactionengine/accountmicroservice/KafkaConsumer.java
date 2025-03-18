package com.swami.transactionengine.accountmicroservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component

public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "account-topic", groupId = "account-group")
    public void listen(String message) {
        log.info("Consumed Kafka message: " + message);
    }

}
