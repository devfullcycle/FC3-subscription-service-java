package com.fullcycle.subscription.infrastructure.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping(value = "internal/events")
@Tag(name = "Internal Events")
public interface InternalEventRestApi {

    @GetMapping(
            value = "aggregate",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<List> allEventsOfAggregate(@RequestParam String aggregateId, @RequestParam String aggregateType);
}