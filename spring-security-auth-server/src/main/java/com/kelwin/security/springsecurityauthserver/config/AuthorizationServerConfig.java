package com.kelwin.security.springsecurityauthserver.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
@EnableJpaRepositories("com.kelwin.security.springsecurityauthserver.model")
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;
//
    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${oauth.config.accessTokenValiditySeconds:3600}")
    private int accessTokenValiditySeconds;
    @Value("${oauth.config.clientId:clientId}")
    private String clientId;
    @Value("${oauth.config.clientSecret:clientSecret}")
    private String clientSecret;
    @Value("${oauth.config.authorizedGrantType.main-type:client_credentials}")
    private String authorizedGrantTypesMainType;

    @Value("${oauth.config.authorizedGrantType.refresh:refresh_token}")
    private String authorizedGrantTypeRefresh;

    @Value("${oauth.config.scopes:user_info}")
    private String scopes;

    @Value("${signing-key:1234}")
    private String signingKey;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    //Bean
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        final JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(signingKey);
        return jwtAccessTokenConverter;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }


    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        return tokenServices;
    }

    //end-of: Bean


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(clientId)
                .secret(clientSecret)
                .authorizedGrantTypes(authorizedGrantTypesMainType)
                .scopes(scopes)
                .autoApprove(true)
                .accessTokenValiditySeconds(accessTokenValiditySeconds)

                .and()

                .withClient("refreshClientId")
                .secret("refreshSecretId")
                .authorizedGrantTypes(authorizedGrantTypesMainType, authorizedGrantTypeRefresh)
                .scopes(scopes, "read", "write", "trust")
                .autoApprove(true)
                .accessTokenValiditySeconds(accessTokenValiditySeconds * 24)


        ;
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)
                .accessTokenConverter(accessTokenConverter())
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .accessTokenConverter(accessTokenConverter())

        ;

    }
}
