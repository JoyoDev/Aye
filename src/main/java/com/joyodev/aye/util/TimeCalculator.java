package com.joyodev.aye.util;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeCalculator {

    public static long calculateTimeSince(LocalDateTime time) {
        return Duration.between(time, LocalDateTime.now()).toMinutes();
    }
}
