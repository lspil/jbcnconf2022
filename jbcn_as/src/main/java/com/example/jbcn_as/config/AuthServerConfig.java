package com.example.jbcn_as.config;

import com.example.jbcn_as.config.keys.KeyManager;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;

@Configuration
@AllArgsConstructor
public class AuthServerConfig {

  private final KeyManager keyManager;

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public SecurityFilterChain securityFilterChainAuthServer(HttpSecurity http) throws Exception {
    OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
    return http.formLogin()
        .and().build();
  }

  @Bean
  public RegisteredClientRepository registeredClientRepository() {
    var c = RegisteredClient.withId(UUID.randomUUID().toString())
        .clientId("client")
        .clientSecret("secret")
        .redirectUri("http://iamawesome.com")
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
        .scope(OidcScopes.OPENID)
        .build();

    return new InMemoryRegisteredClientRepository(c);
  }

  @Bean
  public ProviderSettings providerSettings() {
    return ProviderSettings.builder().build();
  }

  @Bean
  public JWKSource<SecurityContext> jwkSource() {
    JWKSet set = new JWKSet(keyManager.getKey());
    return (jsc, sc) -> jsc.select(set);
  }
}
