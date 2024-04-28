package com.fullcycle.subscription.infrastructure.authentication.principal;

import com.fullcycle.subscription.domain.account.Account;
import com.fullcycle.subscription.domain.account.idp.UserId;

import java.util.Optional;
import java.util.function.Function;

public interface AccountFromUserIdResolver extends Function<UserId, Optional<Account>> {
}
