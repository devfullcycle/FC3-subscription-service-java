package com.fullcycle.codeflix.subscription.infrastructure.repositories;

import com.fullcycle.codeflix.subscription.infrastructure.repositories.entities.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, String> {
}