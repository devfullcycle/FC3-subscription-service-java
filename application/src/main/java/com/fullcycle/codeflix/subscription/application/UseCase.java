package com.fullcycle.codeflix.subscription.application;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN anIn);

    public <T> T execute(IN anIn, Presenter<OUT, T> presenter) {
        if (presenter == null) {
            throw new IllegalArgumentException("presenter is required");
        }
        return presenter.apply(execute(anIn));
    }
}