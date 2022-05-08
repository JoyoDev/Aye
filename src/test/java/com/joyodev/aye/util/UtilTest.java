package com.joyodev.aye.util;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class UtilTest {

    @Test
    public void urlIsNotValid() {
        assertFalse(URLValidator.validateUrl("some.bad.url/v1"));
    }

    @Test
    public void urlIsValid() {
        assertTrue(URLValidator.validateUrl("https://localhost:8228/v1"));
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
