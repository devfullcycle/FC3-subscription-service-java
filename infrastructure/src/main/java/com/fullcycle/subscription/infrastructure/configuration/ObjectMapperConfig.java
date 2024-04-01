package com.fullcycle.subscription.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.subscription.infrastructure.json.Json;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Bean;

@JsonComponent
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return Json.mapper();
    }
}
