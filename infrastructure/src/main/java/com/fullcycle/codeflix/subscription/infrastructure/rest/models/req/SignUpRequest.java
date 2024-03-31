package com.fullcycle.codeflix.subscription.infrastructure.rest.models.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public record SignUpRequest(
        @NotEmpty @Max(255) String firstname,
        @NotEmpty @Max(255) String lastname,
        @NotEmpty @Max(255) String nickname,
        @NotEmpty @Email String email,
        @NotEmpty @Min(12) @Max(20) String password,
        @NotEmpty String documentNumber,
        @NotEmpty String documentType
) {

}
