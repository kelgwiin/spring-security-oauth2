package com.kelwin.security.springsecurityauthserver.controller;

import com.kelwin.security.springsecurityauthserver.model.Item;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/secure")
@CrossOrigin(origins = "*")
public class HelloResource {


    @GetMapping("/principal")
    public Principal user(Principal principal) {
        return principal;
    }
    @GetMapping("/hello")
    public Item hello() {
        Item item = new Item();
        item.setName("Hello world");
        item.setVersion("v1");

        return item;
    }



}
