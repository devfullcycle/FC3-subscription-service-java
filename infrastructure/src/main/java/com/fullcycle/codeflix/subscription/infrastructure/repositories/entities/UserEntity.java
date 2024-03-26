package com.fullcycle.codeflix.subscription.infrastructure.repositories.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "User")
@Table(name = "users")
public class UserEntity {

    @Id
    private String id;

}
