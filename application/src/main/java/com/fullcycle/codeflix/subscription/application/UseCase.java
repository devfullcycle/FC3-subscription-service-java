package com.fullcycle.codeflix.subscription.application;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN anIn);
}