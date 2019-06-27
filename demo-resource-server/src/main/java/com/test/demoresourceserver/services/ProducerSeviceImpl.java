package com.test.demoresourceserver.services;

import com.test.demoresourceserver.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProducerSeviceImpl implements ProducerService {
    @Value("${kafka.topicname}")
    private String topic;

    private KafkaTemplate<String, Customer> kafkaTemplate;


    @Autowired
    public void setKafkaTemplate(KafkaTemplate<String, Customer> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(Customer message) {
        log.info(String.format("$$ -> Producing message --> %s", message.toString()));
        kafkaTemplate.send(topic, message);
    }


}
