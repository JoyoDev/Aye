package com.joyodev.aye.activescanner;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ActiveScannerClientTest {

    @Test
    void canCreateClientWithValidURL() {
        assertDoesNotThrow(() -> new ActiveScannerClient("https://localhost:8228/v1"));
    }

    @Test
    void cannotCreateClientWithInvalidURL() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ActiveScannerClient("some.bad.url/v1");
        });
        String expectedMessage = "Provided Anchore Engine URL is not valid: some.bad.url/v1";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

}
