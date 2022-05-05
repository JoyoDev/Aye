package com.joyodev.aye.activescanner;

import com.joyodev.aye.utils.URLValidator;
import lombok.Getter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * Anchore Client that checks if Anchore Engine can be reached
 */
public class ActiveScannerClient implements Client {

    @Getter
    private String engineUrl;

    private HttpClient httpClient;

    public ActiveScannerClient(String engineUrl) {
        if(!URLValidator.validateUrl(engineUrl)) {
            throw new IllegalArgumentException("Provided Anchore Engine URL is not valid: " + engineUrl);
        }
        this.engineUrl = engineUrl;
        this.httpClient = HttpClient.newBuilder().build();
    }

    @Override
    public boolean checkEngineHealth() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(this.engineUrl))
                    .timeout(Duration.of(10, ChronoUnit.SECONDS))
                    .GET()
                    .build();
            HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
