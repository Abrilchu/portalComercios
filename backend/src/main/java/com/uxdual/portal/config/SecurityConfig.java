package com.uxdual.portal.config;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.jwt.generator.JwtTokenGenerator;
import io.micronaut.security.token.jwt.signature.secret.SecretSignatureConfiguration;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Factory
public class SecurityConfig {

    @Bean
    @Singleton
    @Named("generator")
    SecretSignatureConfiguration secretSignatureConfiguration() {
        return new SecretSignatureConfiguration("pleasechangethissecretkey");
    }
}
