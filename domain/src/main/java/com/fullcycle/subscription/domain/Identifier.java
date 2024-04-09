package com.fullcycle.subscription.domain;

public interface Identifier<T> extends ValueObject {
    T value();
}
