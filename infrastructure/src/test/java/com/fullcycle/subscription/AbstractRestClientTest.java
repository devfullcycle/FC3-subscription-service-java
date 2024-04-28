package com.fullcycle.subscription;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.subscription.infrastructure.configuration.WebServerConfig;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test-integration")
@AutoConfigureWireMock(port = 0)
@SpringBootTest(classes = {WebServerConfig.class, IntegrationTestConfiguration.class})
@Tag("integrationTest")
public abstract class AbstractRestClientTest extends AbstractTest {

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach() {
        WireMock.reset();
        WireMock.resetAllRequests();
    }
}
