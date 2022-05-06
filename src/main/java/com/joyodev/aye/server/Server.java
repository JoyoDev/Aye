package com.joyodev.aye.server;

import com.joyodev.aye.activescanner.ActiveScannerClient;
import com.joyodev.aye.activescanner.Client;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.util.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.IOException;

@RestController
public class Server {

    @Value("${ANCHORE_ENGINE_URL:http://localhost:8228/v1/}")
    private String anchoreEngineUrl;

    private Client activeScannerClient;

    private ApiClient k8sClientSet;

    @PostConstruct
    public void initialize() throws IOException {
        //k8sClientSet = Config.defaultClient();
        activeScannerClient = new ActiveScannerClient(anchoreEngineUrl);
    }

    @GetMapping("/healthz")
    public String health() {
        return "healthy!";
    }

}
