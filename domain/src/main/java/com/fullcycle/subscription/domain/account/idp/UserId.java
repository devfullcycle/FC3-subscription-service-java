package com.fullcycle.subscription.domain.account.idp;

import com.fullcycle.subscription.domain.Identifier;

public record UserId(String value) implements Identifier<String> {

    public UserId {
        this.assertArgumentNotEmpty(value, "'userId' should not be empty");
    }

    public static UserId empty() {
        return new UserId("");
    }
}
