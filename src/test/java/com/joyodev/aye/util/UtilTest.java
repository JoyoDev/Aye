package com.joyodev.aye.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.LocalTime;

@SpringBootTest
public class UtilTest {

    @Test
    public void urlIsNotValid() {
        assertEquals(URLValidator.validateUrl("some.bad.url/v1"), false);
    }

    @Test
    public void urlIsValid() {
        assertEquals(URLValidator.validateUrl("https://localhost:8228/v1"), true);
    }

    @Test void timeCalculatorReturnsCorrectValueWhenMoreMinutesPassed() {
        // given
        LocalDateTime time = LocalDateTime.now().minusMinutes(20);

        // when
        long difference = TimeCalculator.calculateTimeSince(time);

        // then
        assertTrue(difference > 15);
    }

    @Test void timeCalculatorReturnsIncorrectValueWhenLessMinutesPassed() {
        // given
        LocalDateTime time = LocalDateTime.now().minusMinutes(10);

        // when
        long difference = TimeCalculator.calculateTimeSince(time);

        // then
        assertTrue(difference < 15);
    }
}
