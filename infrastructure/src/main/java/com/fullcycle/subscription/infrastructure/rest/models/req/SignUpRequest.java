package com.fullcycle.subscription.infrastructure.rest.models.req;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullcycle.subscription.application.account.CreateAccount;
import com.fullcycle.subscription.application.account.CreateIdpUser;
import com.fullcycle.subscription.domain.account.AccountId;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignUpRequest implements CreateIdpUser.Input, CreateAccount.Input {

    @NotBlank
    @Size(max = 255)
    private final String firstname;

    @NotBlank
    @Size(max = 255)
    private final String lastname;

    @NotBlank
    @Email
    @Size(max = 255)
    private final String email;

    @NotBlank
    @Size(max = 28)
    private final String password;

    @NotBlank
    private final String documentType;

    @NotBlank
    @Size(min = 11, max = 14)
    private final String documentNumber;

    private final String userId;
    private final String accountId;

    private SignUpRequest(
            String documentNumber,
            String documentType,
            String password,
            String email,
            String lastname,
            String firstname,
            String userId,
            String accountId
    ) {
        this.documentNumber = documentNumber;
        this.documentType = documentType;
        this.password = password;
        this.email = email;
        this.lastname = lastname;
        this.firstname = firstname;
        this.userId = userId;
        this.accountId = accountId;
    }

    @JsonCreator
    public SignUpRequest(
            @JsonProperty("document_number") String documentNumber,
            @JsonProperty("document_type") String documentType,
            @JsonProperty("password") String password,
            @JsonProperty("email") String email,
            @JsonProperty("lastname") String lastname,
            @JsonProperty("firstname") String firstname
    ) {
        this(
                documentNumber,
                documentType,
                password,
                email,
                lastname,
                firstname,
                "",
                ""
        );
    }

    @Override
    public String firstname() {
        return firstname;
    }

    @Override
    public String lastname() {
        return lastname;
    }

    @Override
    public String email() {
        return email;
    }

    @Override
    public String password() {
        return password;
    }

    public String documentType() {
        return documentType;
    }

    public String documentNumber() {
        return documentNumber;
    }

    public String userId() {
        return userId;
    }

    @Override
    public String accountId() {
        return accountId;
    }

    public SignUpRequest with(final AccountId accountId) {
        return new SignUpRequest(
                documentNumber(),
                documentType(),
                password(),
                email(),
                lastname(),
                firstname(),
                userId(),
                accountId.value()
        );
    }

    public SignUpRequest with(final CreateIdpUser.Output out) {
        return new SignUpRequest(
                documentNumber(),
                documentType(),
                password(),
                email(),
                lastname(),
                firstname(),
                out.idpUserId().value(),
                accountId()
        );
    }
}
