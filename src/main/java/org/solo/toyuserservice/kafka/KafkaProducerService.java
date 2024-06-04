package org.solo.toyuserservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {

        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String message) {

        kafkaTemplate.send(topic, message);
        System.out.println("Message send to Kafka:" + message);
    }
}
