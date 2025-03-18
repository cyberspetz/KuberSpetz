package com.swami.transactionengine.kuberspetz.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "idv-topic", groupId = "kuber-spetz-group")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }
}
