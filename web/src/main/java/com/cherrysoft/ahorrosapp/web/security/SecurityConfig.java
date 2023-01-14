package com.cherrysoft.ahorrosapp.web.security;

import com.cherrysoft.ahorrosapp.web.security.service.SecurityUserDetailsService;
import com.cherrysoft.ahorrosapp.web.security.support.KeyPairProvider;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@Import(SecurityProblemSupport.class)
public class SecurityConfig {
  private static final String[] AUTH_WHITELIST = {
      "/login",
      "/register",
      "/refresh-token",
      "/swagger-ui/**",
      "/swagger-resources/**",
      "/v3/api-docs/**"
  };
  private final JwtToSecurityUserConverter jwtToUserConverter;
  private final KeyPairProvider keyPairProvider;
  private final PasswordEncoder passwordEncoder;
  private final SecurityProblemSupport securityProblemSupport;

  @Bean
  @Qualifier("jwtRefreshTokenAuthProvider")
  JwtAuthenticationProvider jwtRefreshTokenAuthProvider() {
    JwtAuthenticationProvider provider = new JwtAuthenticationProvider(jwtRefreshTokenDecoder());
    provider.setJwtAuthenticationConverter(jwtToUserConverter);
    return provider;
  }

  @Bean
  DaoAuthenticationProvider authManager(SecurityUserDetailsService userDetailsService) {
    var authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder);
    return authProvider;
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeRequests(auth -> auth
            .antMatchers(AUTH_WHITELIST).permitAll()
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .oauth2ResourceServer(oauth2 ->
            oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtToUserConverter))
        )
        .exceptionHandling((exceptions) -> exceptions
            .authenticationEntryPoint(securityProblemSupport)
            .accessDeniedHandler(securityProblemSupport)
        )
        .build();
  }

  @Bean
  @Primary
  JwtEncoder jwtEncoder() {
    JWK jwk = new RSAKey
        .Builder(keyPairProvider.getAccessTokenPublicKey())
        .privateKey(keyPairProvider.getAccessTokenPrivateKey())
        .build();
    return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(jwk)));
  }

  @Bean
  @Primary
  JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder
        .withPublicKey(keyPairProvider.getAccessTokenPublicKey())
        .build();
  }

  @Bean
  @Qualifier("jwtRefreshTokenEncoder")
  JwtEncoder jwtRefreshTokenEncoder() {
    JWK jwk = new RSAKey
        .Builder(keyPairProvider.getRefreshTokenPublicKey())
        .privateKey(keyPairProvider.getRefreshTokenPrivateKey())
        .build();
    return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(jwk)));
  }

  @Bean
  @Qualifier("jwtRefreshTokenDecoder")
  JwtDecoder jwtRefreshTokenDecoder() {
    return NimbusJwtDecoder
        .withPublicKey(keyPairProvider.getRefreshTokenPublicKey())
        .build();
  }

}
