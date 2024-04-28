package com.fullcycle.subscription;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.TimeZone;

public class TimeZoneSetup implements BeforeAllCallback {
    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
