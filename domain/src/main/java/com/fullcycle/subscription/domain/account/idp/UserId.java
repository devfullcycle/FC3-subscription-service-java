package com.fullcycle.subscription.domain.account.idp;

import com.fullcycle.subscription.domain.Identifier;

import java.util.Objects;

public final class UserId implements Identifier<String> {

    private final String value;

    private UserId() {
        this.value = "";
    }

    public UserId(final String value) {
        this.assertArgumentNotEmpty(value, "'userId' should not be empty");
        this.value = value;
    }

    public static UserId empty() {
        return new UserId();
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UserId) obj;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "UserId[" +
                "value=" + value + ']';
    }

}
