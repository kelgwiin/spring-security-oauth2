package com.test.demoresourceserver.services;

import com.test.demoresourceserver.model.Customer;

public interface ProducerService {
    void sendMessage(Customer customer);
}
