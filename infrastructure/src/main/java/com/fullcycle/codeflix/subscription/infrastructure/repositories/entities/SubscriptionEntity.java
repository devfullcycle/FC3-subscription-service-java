package com.fullcycle.codeflix.subscription.infrastructure.repositories.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "Subscription")
@Table(name = "subscriptions")
public class SubscriptionEntity {

    @Id
    private String id;

}
