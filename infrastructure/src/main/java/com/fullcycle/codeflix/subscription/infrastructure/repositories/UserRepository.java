package com.fullcycle.codeflix.subscription.infrastructure.repositories;

import com.fullcycle.codeflix.subscription.infrastructure.repositories.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}