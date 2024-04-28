package com.fullcycle.subscription.infrastructure.rest;

import com.fullcycle.subscription.infrastructure.authentication.principal.CodeflixUser;
import com.fullcycle.subscription.infrastructure.rest.models.req.ChargeSubscriptionRequest;
import com.fullcycle.subscription.infrastructure.rest.models.req.CreateSubscriptionRequest;
import com.fullcycle.subscription.infrastructure.rest.models.res.CancelSubscriptionResponse;
import com.fullcycle.subscription.infrastructure.rest.models.res.ChargeSubscriptionResponse;
import com.fullcycle.subscription.infrastructure.rest.models.res.CreateSubscriptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "subscriptions")
@Tag(name = "Subscription")
public interface SubscriptionRestApi {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new subscription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was observed"),
            @ApiResponse(responseCode = "500", description = "An unpredictable error was observed"),
    })
    ResponseEntity<CreateSubscriptionResponse> createSubscription(
            @RequestBody @Valid CreateSubscriptionRequest req,
            @AuthenticationPrincipal final CodeflixUser principal
    );

    @PutMapping(
            value = "active/cancel",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Cancel an active subscription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Canceled successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was observed"),
            @ApiResponse(responseCode = "500", description = "An unpredictable error was observed"),
    })
    ResponseEntity<CancelSubscriptionResponse> cancelSubscription(@AuthenticationPrincipal final CodeflixUser principal);

    @PutMapping(
            value = "active/charge",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Charge an active subscription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Charged successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was observed"),
            @ApiResponse(responseCode = "500", description = "An unpredictable error was observed"),
    })
    ResponseEntity<ChargeSubscriptionResponse> chargeActiveSubscription(
            @RequestBody @Valid ChargeSubscriptionRequest req,
            @AuthenticationPrincipal final CodeflixUser principal
    );
}