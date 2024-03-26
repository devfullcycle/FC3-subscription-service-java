package com.fullcycle.codeflix.subscription.application;

public abstract class NullaryUseCase<OUT> {

    public abstract OUT execute();

    public <T> T execute(Presenter<OUT, T> presenter) {
        return presenter.apply(execute());
    }
}
