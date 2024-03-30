package com.fullcycle.codeflix.subscription.infrastructure.gateways.repositories;

import com.fullcycle.codeflix.subscription.infrastructure.gateways.repositories.entities.SubscriptionEntity;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepository extends CrudRepository<SubscriptionEntity, String> {
}