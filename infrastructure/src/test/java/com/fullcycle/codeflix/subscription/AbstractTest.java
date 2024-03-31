package com.fullcycle.codeflix.subscription;

import org.junit.jupiter.api.BeforeAll;

import java.util.TimeZone;

public abstract class AbstractTest {

    @BeforeAll
    static void beforeAll() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
