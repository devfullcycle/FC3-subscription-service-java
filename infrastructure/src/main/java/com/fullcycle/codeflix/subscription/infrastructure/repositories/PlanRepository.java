package com.fullcycle.codeflix.subscription.infrastructure.repositories;

import com.fullcycle.codeflix.subscription.infrastructure.repositories.entities.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<PlanEntity, String> {
}