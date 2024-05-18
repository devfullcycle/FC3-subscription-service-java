package com.fullcycle.subscription.infrastructure.rest;

import com.fullcycle.subscription.infrastructure.authentication.principal.CodeflixUser;
import com.fullcycle.subscription.infrastructure.rest.models.req.BillingInfoRequest;
import com.fullcycle.subscription.infrastructure.rest.models.req.SignUpRequest;
import com.fullcycle.subscription.infrastructure.rest.models.res.BillingInfoResponse;
import com.fullcycle.subscription.infrastructure.rest.models.res.SignUpResponse;
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

@RequestMapping(value = "accounts")
@Tag(name = "Account")
public interface AccountRestApi {

    @PostMapping(
            value = "sign-up",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was observed"),
            @ApiResponse(responseCode = "500", description = "An unpredictable error was observed"),
    })
    ResponseEntity<SignUpResponse> signUp(@RequestBody @Valid SignUpRequest req);

    @PutMapping(
            value = "{accountId}/billing-info",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update account billing information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Updated successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was observed"),
            @ApiResponse(responseCode = "500", description = "An unpredictable error was observed"),
    })
    ResponseEntity<BillingInfoResponse> updateBillingInfo(
            @AuthenticationPrincipal final CodeflixUser principal,
            @RequestBody @Valid BillingInfoRequest req
    );
}