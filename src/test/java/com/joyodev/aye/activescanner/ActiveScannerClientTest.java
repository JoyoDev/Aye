package com.joyodev.aye.activescanner;
import static org.junit.jupiter.api.Assertions.*;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class ActiveScannerClientTest {

    private MockWebServer mockWebServer;

    @BeforeEach
    public void setup() throws IOException {
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start();
    }

    @Test
    public void canCreateClientWithValidURL() {
        assertDoesNotThrow(() -> new ActiveScannerClient("https://localhost:8228/v1/"));
    }

    @Test
    public void cannotCreateClientWithInvalidURL() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ActiveScannerClient("some.bad.url/v1");
        });
        String expectedMessage = "Provided Anchore Engine URL is not valid: some.bad.url/v1/";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void engineIsUnhealthy() {
        // given
        ActiveScannerClient activeScannerClient = new ActiveScannerClient(mockWebServer.url("/").toString());

        // when
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setResponseCode(500));
        boolean isHealthy = activeScannerClient.checkEngineHealth();

        // then
        assertEquals(isHealthy, false);
    }

    @Test
    public void engineIsHealthy() {
        // given
        ActiveScannerClient activeScannerClient = new ActiveScannerClient(mockWebServer.url("/").toString());

        // when
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setResponseCode(200));
        boolean isHealthy = activeScannerClient.checkEngineHealth();

        // then
        assertEquals(isHealthy, true);
    }

    @AfterEach
    public void teardown() throws IOException {
        mockWebServer.shutdown();
    }

}
