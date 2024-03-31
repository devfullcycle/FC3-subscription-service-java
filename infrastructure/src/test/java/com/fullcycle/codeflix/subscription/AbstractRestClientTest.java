package com.fullcycle.codeflix.subscription;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.codeflix.subscription.infrastructure.authentication.GetClientCredentials;
import com.fullcycle.codeflix.subscription.infrastructure.configuration.RestClientConfig;
import com.fullcycle.codeflix.subscription.infrastructure.configuration.properties.KeycloakProperties;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.springboot3.bulkhead.autoconfigure.BulkheadAutoConfiguration;
import io.github.resilience4j.springboot3.circuitbreaker.autoconfigure.CircuitBreakerAutoConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@ActiveProfiles("test-integration")
@AutoConfigureWireMock(port = 0)
@EnableConfigurationProperties(value = {
        KeycloakProperties.class
})
@ImportAutoConfiguration(classes = {
        BulkheadAutoConfiguration.class,
        CacheAutoConfiguration.class,
        CircuitBreakerAutoConfiguration.class,
})
@RestClientTest(
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*Client"),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = RestClientConfig.class),
        }
)
@Tag("integrationTest")
public abstract class AbstractRestClientTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BulkheadRegistry bulkheadRegistry;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired(required = false)
    private CacheManager cacheManager;

    @MockBean
    protected GetClientCredentials getClientCredentials;

    @BeforeEach
    void beforeEach() {
        WireMock.reset();
        WireMock.resetAllRequests();
        resetAllCaches();
        List.of("").forEach(this::resetFaultTolerance);
    }

    protected Cache cache(final String name) {
        if (cacheManager == null) {
            return null;
        }
        return cacheManager.getCache(name);
    }

    protected void checkCircuitBreakerState(final String name, final CircuitBreaker.State expectedState) {
        final var cb = circuitBreakerRegistry.circuitBreaker(name);
        Assertions.assertEquals(expectedState, cb.getState());
    }

    protected void acquireBulkheadPermission(final String name) {
        bulkheadRegistry.bulkhead(name).acquirePermission();
    }

    protected void releaseBulkheadPermission(final String name) {
        bulkheadRegistry.bulkhead(name).releasePermission();
    }

    protected void transitionToOpenState(final String name) {
        circuitBreakerRegistry.circuitBreaker(name).transitionToOpenState();
    }

    protected void transitionToClosedState(final String name) {
        circuitBreakerRegistry.circuitBreaker(name).transitionToClosedState();
    }

    protected String writeValueAsString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void resetAllCaches() {
        if (cacheManager == null) {
            return;
        }
        cacheManager.getCacheNames().forEach(name -> cacheManager.getCache(name).clear());
    }

    private void resetFaultTolerance(final String name) {
        circuitBreakerRegistry.circuitBreaker(name).reset();
    }
}
