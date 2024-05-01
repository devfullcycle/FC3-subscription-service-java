package com.fullcycle.subscription.infrastructure.job;

import com.fullcycle.subscription.infrastructure.authentication.clientcredentials.RefreshClientCredentials;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ClientCredentialsRefreshJob {

    private final RefreshClientCredentials refreshApplicationToken;

    public ClientCredentialsRefreshJob(final RefreshClientCredentials refreshApplicationToken) {
        this.refreshApplicationToken = refreshApplicationToken;
    }

    @Scheduled(fixedRate = 3, timeUnit = TimeUnit.MINUTES)
    public void refreshTokenJob() {
        this.refreshApplicationToken.refresh();
    }
}
