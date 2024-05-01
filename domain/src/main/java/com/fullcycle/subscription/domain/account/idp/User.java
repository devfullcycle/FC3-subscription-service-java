package com.fullcycle.subscription.domain.account.idp;

import com.fullcycle.subscription.domain.AssertionConcern;
import com.fullcycle.subscription.domain.account.AccountId;
import com.fullcycle.subscription.domain.person.Email;
import com.fullcycle.subscription.domain.person.Name;

public class User implements AssertionConcern {

    private final UserId userId;
    private AccountId accountId;
    private Name name;
    private Email email;
    private boolean enabled;
    private boolean emailVerified;
    private String password;

    private User(
            final UserId userId,
            final AccountId accountId,
            final Name name,
            final Email email,
            final Boolean enabled,
            final Boolean emailVerified
    ) {
        this.userId = userId;
        this.setAccountId(accountId);
        this.setName(name);
        this.setEmail(email);
        this.setEnabled(enabled);
        this.setEmailVerified(emailVerified);
    }

    private User(
            final UserId userId,
            final AccountId accountId,
            final Name name,
            final Email email,
            final Boolean enabled,
            final Boolean emailVerified,
            final String password
    ) {
        this(userId, accountId, name, email, enabled, emailVerified);
        this.setPassword(this.assertArgumentNotEmpty(password, "User 'password' cannot be empty for new users"));
    }

    public static User newUser(final AccountId accountId, final Name name, final Email email, final String password) {
        return new User(UserId.empty(), accountId, name, email, true, false, password);
    }

    public static User with(final UserId id, final AccountId accountId, final Name name, final Email email, final Boolean enabled, final Boolean emailVerified) {
        return new User(id, accountId, name, email, enabled, emailVerified);
    }

    public UserId userId() {
        return userId;
    }

    public AccountId accountId() {
        return accountId;
    }

    public Name name() {
        return name;
    }

    public Email email() {
        return email;
    }

    public boolean enabled() {
        return enabled;
    }

    public boolean emailVerified() {
        return emailVerified;
    }

    public String password() {
        return password;
    }

    public void setAccountId(AccountId accountId) {
        this.assertArgumentNotNull(accountId, "User 'accountId' should not be null");
        this.accountId = accountId;
    }

    private void setName(final Name name) {
        this.assertArgumentNotNull(name, "User 'name' should not be null");
        this.name = name;
    }

    private void setEmail(final Email email) {
        this.assertArgumentNotNull(email, "User 'email' should not be null");
        this.email = email;
    }

    private void setEnabled(Boolean enabled) {
        this.enabled = enabled != null ? enabled : true;
    }

    private void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified != null ? emailVerified : false;
    }

    private void setPassword(String password) {
        this.password = password;
    }
}
