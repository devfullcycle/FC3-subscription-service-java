package com.fullcycle.subscription;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;

import java.util.function.Consumer;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

public interface ApiTest {

    static JwtRequestPostProcessor admin() {
        return jwt()
                .jwt(builder -> builder.claim("accountId", "123"))
                .authorities(new SimpleGrantedAuthority("ROLE_SUBSCRIPTION_ADMIN"));
    }
}
