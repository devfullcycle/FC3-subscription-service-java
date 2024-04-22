package com.fullcycle.subscription.application;

public abstract class NullaryUseCase<OUT> {

    public abstract OUT execute();

    public <T> T execute(Presenter<OUT, T> presenter) {
        if (presenter == null) {
            throw new IllegalArgumentException("NullaryUseCase 'presenter' is required");
        }

        return presenter.apply(execute());
    }
}
