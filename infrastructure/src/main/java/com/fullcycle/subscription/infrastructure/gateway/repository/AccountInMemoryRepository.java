package com.fullcycle.subscription.infrastructure.gateway.repository;

import com.fullcycle.subscription.domain.account.Account;
import com.fullcycle.subscription.domain.account.AccountGateway;
import com.fullcycle.subscription.domain.account.AccountId;
import com.fullcycle.subscription.domain.utils.IdUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AccountInMemoryRepository implements AccountGateway {

    private Map<String, Account> db = new ConcurrentHashMap<>();

    @Override
    public AccountId nextId() {
        return new AccountId(IdUtils.uniqueId());
    }

    @Override
    public Optional<Account> accountOfId(AccountId anId) {
        return Optional.ofNullable(this.db.get(anId.value()));
    }

    @Override
    public Account save(Account anAccount) {
        this.db.put(anAccount.id().value(), anAccount);
        return anAccount;
    }
}
