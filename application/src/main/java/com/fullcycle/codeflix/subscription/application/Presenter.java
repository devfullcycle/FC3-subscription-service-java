package com.fullcycle.codeflix.subscription.application;

import java.util.function.Function;

public interface Presenter<OUT, T> extends Function<OUT, T> {
}
