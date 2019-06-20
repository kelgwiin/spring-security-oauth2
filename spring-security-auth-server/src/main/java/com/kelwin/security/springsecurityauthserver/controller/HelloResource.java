package com.kelwin.security.springsecurityauthserver.controller;

import com.kelwin.security.springsecurityauthserver.model.Item;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/secure")
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
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


    @GetMapping("/hello-admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Item helloAdmin() {
        Item item = new Item();
        item.setName("Hello world Admin");
        item.setVersion("v1");

        return item;
    }

    @GetMapping("/hello-scope")
    @PreAuthorize("#oauth2.hasScope('test-scope')")
    public Item helloScope() {
        Item item = new Item();
        item.setName("Hello world Scope");
        item.setVersion("v1");

        return item;
    }



}
