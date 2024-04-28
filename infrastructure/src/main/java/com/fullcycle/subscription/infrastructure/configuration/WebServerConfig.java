package com.fullcycle.subscription.infrastructure.configuration;

import com.fullcycle.subscription.infrastructure.authentication.clientcredentials.RefreshClientCredentials;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.Clock;

@Configuration(proxyBeanMethods = false)
@ComponentScan("com.fullcycle.subscription")
@EnableScheduling
public class WebServerConfig {


    @Bean
    @Profile("!test-integration && !test-e2e")
    ApplicationListener<ContextRefreshedEvent> refreshClientCredentials(final RefreshClientCredentials refreshClientCredentials) {
        return ev -> {
            refreshClientCredentials.refresh();
        };
    }

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    LocalValidatorFactoryBean localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }
}