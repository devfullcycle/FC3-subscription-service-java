package com.fullcycle.subscription.application.account.impl;

import com.fullcycle.subscription.application.account.UpdateBillingInfo;
import com.fullcycle.subscription.domain.account.Account;
import com.fullcycle.subscription.domain.account.AccountCommand;
import com.fullcycle.subscription.domain.account.AccountGateway;
import com.fullcycle.subscription.domain.account.AccountId;
import com.fullcycle.subscription.domain.exceptions.DomainException;
import com.fullcycle.subscription.domain.person.Address;

import java.util.Objects;

public class DefaultUpdateBillingInfo extends UpdateBillingInfo {

    private final AccountGateway accountGateway;

    public DefaultUpdateBillingInfo(final AccountGateway accountGateway) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
    }

    @Override
    public Output execute(final Input input) {
        if (input == null) {
            throw DomainException.with("Invalid input");
        }

        final var anAccountId = new AccountId(input.accountId());

        final var anAccount = this.accountGateway.accountOfId(anAccountId)
                .orElseThrow(() -> DomainException.notFound(Account.class, anAccountId));

        anAccount.execute(new AccountCommand.ChangeProfileCommand(anAccount.name(), newBillingAddress(input)));

        this.accountGateway.save(anAccount);
        return new StdOutput(anAccountId);
    }

    private Address newBillingAddress(final Input input) {
        return new Address(input.zipcode(), input.number(), input.complement(), input.country());
    }

    record StdOutput(AccountId accountId) implements Output {}
}
