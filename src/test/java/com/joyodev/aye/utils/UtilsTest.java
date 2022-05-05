package com.joyodev.aye.utils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UtilsTest {

    @Test
    public void urlIsNotValid() {
        assertEquals(URLValidator.validateUrl("some.bad.url/v1"), false);
    }

    @Test
    public void urlIsValid() {
        assertEquals(URLValidator.validateUrl("https://localhost:8228/v1"), true);
    }
}
