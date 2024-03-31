package com.fullcycle.codeflix.subscription.infrastructure;

import com.fullcycle.codeflix.subscription.AbstractTest;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeZoneTest extends AbstractTest {

    @Test
    public void testTimeZoneShouldBeUTC() {
        final var calendar = Calendar.getInstance();
        assertEquals(calendar.getTimeZone(), TimeZone.getTimeZone("UTC"));
    }
}
