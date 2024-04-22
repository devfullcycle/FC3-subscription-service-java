package com.fullcycle.subscription.application;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN in);

    public <T> T execute(IN in, Presenter<OUT, T> presenter) {
        if (presenter == null) {
            throw new IllegalArgumentException("UseCase 'presenter' is required");
        }

        return presenter.apply(execute(in));
    }
}