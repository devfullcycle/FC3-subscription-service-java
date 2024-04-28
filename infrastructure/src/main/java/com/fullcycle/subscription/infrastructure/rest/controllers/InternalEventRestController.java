package com.fullcycle.subscription.infrastructure.rest.controllers;

import com.fullcycle.subscription.infrastructure.gateway.repository.EventJdbcRepository;
import com.fullcycle.subscription.infrastructure.rest.InternalEventRestApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InternalEventRestController implements InternalEventRestApi {

    @Autowired
    private EventJdbcRepository eventJdbcRepository;

    @Override
    public ResponseEntity<List> allEventsOfAggregate(String aggregateId, String aggregateType) {
        return ResponseEntity.ok(eventJdbcRepository.allEventsOfAggregate(aggregateId, aggregateType));
    }
}
