package com.fullcycle.codeflix.subscription.infrastructure.configuration;

import com.fullcycle.codeflix.subscription.domain.DomainEvent;
import com.fullcycle.codeflix.subscription.infrastructure.observer.domainevents.AddToSubscribersGroupSubscriber;
import com.fullcycle.codeflix.subscription.infrastructure.observer.Publisher;
import com.fullcycle.codeflix.subscription.infrastructure.observer.domainevents.RemoveFromSubscribersGroupSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class ObserverConfig {

    @Bean
    public Publisher<DomainEvent> domainEventPublisher(
            final AddToSubscribersGroupSubscriber addToSubscribersGroupSubscriber,
            final RemoveFromSubscribersGroupSubscriber removeFromSubscribersGroupSubscriber
    ) {
        final var publisher = new Publisher<DomainEvent>();
        publisher.register(addToSubscribersGroupSubscriber);
        publisher.register(removeFromSubscribersGroupSubscriber);
        return publisher;
    }
}
