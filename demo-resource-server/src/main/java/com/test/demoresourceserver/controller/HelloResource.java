package com.test.demoresourceserver.controller;

import com.test.demoresourceserver.model.Customer;
import com.test.demoresourceserver.services.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class HelloResource {
    private ProducerService producerService;

    public HelloResource(ProducerService producerService){
        this.producerService = producerService;
    }
    @PostMapping("/customer")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void sendCustomerQueue(@RequestBody Customer customer) {
        producerService.sendMessage(customer);
    }
}
