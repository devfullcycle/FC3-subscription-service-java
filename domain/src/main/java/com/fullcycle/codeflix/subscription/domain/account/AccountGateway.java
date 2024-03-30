package com.fullcycle.codeflix.subscription.domain.account;

import java.util.List;
import java.util.Optional;

public interface AccountGateway {

    AccountId nextId();

    List<Account> allAccounts();

    Optional<Account> accountOfId(AccountId accountId);

    Account save(Account user);

}
