package com.fullcycle.codeflix.subscription.infrastructure.repositories.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "Plan")
@Table(name = "plans")
public class PlanEntity {

    @Id
    private String id;

}
