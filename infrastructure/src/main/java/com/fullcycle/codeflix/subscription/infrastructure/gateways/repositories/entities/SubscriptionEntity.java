package com.fullcycle.codeflix.subscription.infrastructure.gateways.repositories.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "subscriptions")
public record SubscriptionEntity(@Id String id) {
}