package com.joyodev.aye.activescanner;

import com.joyodev.aye.utils.URLValidator;
import lombok.Getter;

import java.net.http.HttpClient;

public class ActiveScannerClient implements Client {

    @Getter
    private String engineUrl;

    private HttpClient httpClient;

    public ActiveScannerClient(String engineUrl) {
        if(!URLValidator.validateUrl(engineUrl)) {
            throw new IllegalArgumentException("Anchore Engine URL is not valid: " + engineUrl);
        }
        this.engineUrl = engineUrl;
        this.httpClient = HttpClient.newBuilder().build();
    }

    @Override
    public boolean checkEngineHealth() {
        return false;
    }
}
