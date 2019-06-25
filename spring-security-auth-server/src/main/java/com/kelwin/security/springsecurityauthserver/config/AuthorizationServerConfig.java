package com.kelwin.security.springsecurityauthserver.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    private ApplicationProperties applicationProperties;

    private AuthenticationManager authenticationManager;

    @Autowired
    @Qualifier(value = "myUserDetailsService")
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

    @Value("${oauth.config.scopes:auth}")
    private String scopes;

    @Value("${signing-key:1234}")
    private String signingKey;

    public AuthorizationServerConfig() {
        super();
    }

    public AuthorizationServerConfig(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        this.authenticationManager =
                authenticationConfiguration.getAuthenticationManager();
    }


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()");
        super.configure(security);
    }

    @Autowired
    public void setApplicationProperties(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

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
    @Primary
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(false);
        return tokenServices;
    }

    @SuppressWarnings("deprecation")
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    //end-of: Bean


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        clients.inMemory()
                .withClient(clientId)
                .secret(clientSecret)
                .authorizedGrantTypes(authorizedGrantTypesMainType, authorizedGrantTypeRefresh, "check_token")
                .authorities("ROLE_USER")
                .scopes(scopes, "read", "write", "trust")
                .autoApprove(true)
                .accessTokenValiditySeconds(accessTokenValiditySeconds)


                .and()

                .withClient("AdminClientId")
                .secret("AdminClientSecretId")
                .authorities("ROLE_ADMIN")
                .authorizedGrantTypes(authorizedGrantTypesMainType, authorizedGrantTypeRefresh)
                .refreshTokenValiditySeconds(accessTokenValiditySeconds * 24)
                .scopes(scopes, "read", "write", "trust", "test-admin")
                .autoApprove(true)
                .accessTokenValiditySeconds(accessTokenValiditySeconds)


                .and()
                .withClient("ScopeClientId")
                .secret("ScopeSecretId")
                .authorities("ROLE_USER")
                .authorizedGrantTypes(authorizedGrantTypesMainType, authorizedGrantTypeRefresh)
                .refreshTokenValiditySeconds(accessTokenValiditySeconds * 24)
                .scopes(scopes, "read", "write", "trust", "test-scope")
                .autoApprove(true)
                .accessTokenValiditySeconds(accessTokenValiditySeconds)

        ;
    }


    @SuppressWarnings("RedundantThrows")
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.
                tokenStore(tokenStore()).
                authenticationManager(authenticationManager).
                tokenGranter(endpoints.getTokenGranter()).
                userDetailsService(userDetailsService).
                reuseRefreshTokens(false).
                allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST).
                accessTokenConverter(accessTokenConverter())

                .getFrameworkEndpointHandlerMapping().setCorsConfigurations(corsConfigurationMap())
        ;
    }

    private Map<String, CorsConfiguration> corsConfigurationMap() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        Map<String, CorsConfiguration> configurationMap = new HashMap<>();

        corsConfiguration.setAllowCredentials(applicationProperties.isAllowCredentials());
        corsConfiguration.setAllowedOrigins(Collections.singletonList(applicationProperties.getAllowedOrigins()));
        corsConfiguration.setAllowedMethods(Collections.singletonList(applicationProperties.getAllowedMethods()));
        corsConfiguration.setAllowedHeaders(Collections.singletonList(applicationProperties.getAllowedHeaders()));
        configurationMap.put("/oauth/*", corsConfiguration);

        return configurationMap;
    }


}
