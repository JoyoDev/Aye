package com.joyodev.aye.server;

import com.joyodev.aye.activescanner.ActiveScannerClient;
import com.joyodev.aye.activescanner.Client;
import com.joyodev.aye.exception.ServerNotHealthyException;
import com.joyodev.aye.operator.CLIRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.IOException;

@RestController
@Slf4j
public class Server {

    @Value("${ANCHORE_ENGINE_URL:http://localhost:8228/v1/}")
    private String anchoreEngineUrl;

    private K8sClient k8sClient;

    private Client activeScannerClient;

    @PostConstruct
    public void initialize() throws IOException {
        activeScannerClient = new ActiveScannerClient(anchoreEngineUrl);
        k8sClient = K8sClient.getInstance();
    }

    @GetMapping("/healthz")
    public String health() {
        if(activeScannerClient.checkEngineHealth() && k8sClient.k8sCanListNamespaces() &&
                k8sClient.k8sCanListDeployments() && k8sClient.k8sCanListDaemonSets() && k8sClient.k8sCanListStatefulSets()) {
            return "Healthy!";
        }
        log.warn("Healthcheck failed");
        throw new ServerNotHealthyException();
    }

}
