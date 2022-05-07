package com.joyodev.aye.util;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class TimeCalculator {

    public static long calculateTimeSince(LocalTime time) {
        return ChronoUnit.MINUTES.between(LocalTime.now(), time) % 60;
    }
}
