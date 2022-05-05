package com.joyodev.aye.activescanner;

import lombok.Getter;

import java.net.http.HttpClient;

public class ActiveScannerClient implements Client {

    @Getter
    private String engineUrl;

    private HttpClient httpClient;

    public ActiveScannerClient(String engineUrl) {
        this.engineUrl = engineUrl;
        this.httpClient = HttpClient.newBuilder().build();
    }

    @Override
    public boolean checkEngineHealth() {
        return false;
    }

    @Override
    public String engineUrl() {
        return null;
    }
}
