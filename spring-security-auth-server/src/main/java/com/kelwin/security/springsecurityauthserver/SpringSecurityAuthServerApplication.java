package com.kelwin.security.springsecurityauthserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.kelwin.security.springsecurityauthserver.model")
public class SpringSecurityAuthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityAuthServerApplication.class, args);
	}
}
