package com.kelwin.security.springsecurityauthserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@EnableResourceServer
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    public ResourceServerConfig() {
        super();
    }


//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService);
//        return authenticationProvider;
//    }
//
//    @Bean
//    public AuthenticationProvider authProvider() {
//        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService);
//        return authProvider;
//    }

//    @Autowired
//    public void configureGlobal(final AuthenticationManagerBuilder auth) {
//        auth.authenticationProvider(authProvider());
//    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers()
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/*").permitAll()
                .antMatchers("/api/secure/**").authenticated();

    }

}
