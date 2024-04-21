package com.fullcycle.subscription.infrastructure.rest.models.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotEmpty @Max(255) String firstname,
        @NotEmpty @Max(255) String lastname,
        @NotEmpty @Email @Max(255) String email,
        @NotEmpty @Max(28) String password,
        @NotEmpty String documentType,
        @NotEmpty @Size(min = 11, max = 14) String documentNumber
) {
}
