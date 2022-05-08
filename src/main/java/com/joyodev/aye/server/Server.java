package com.joyodev.aye.server;

import com.joyodev.aye.activescanner.ActiveScannerClient;
import com.joyodev.aye.activescanner.Client;
import com.joyodev.aye.exception.ServerNotHealthyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.IOException;

@RestController
@Slf4j
public class Server {

    @Value("${ANCHORE_CLI_URL:http://localhost:8228/v1/}")
    private String anchoreCliUrl;

    private K8sClient k8sClient;

    private Client client;

    @PostConstruct
    public void initialize() throws IOException {
        client = new ActiveScannerClient(anchoreCliUrl);
        k8sClient = K8sClient.getInstance();
    }

    @GetMapping("/healthz")
    public String health() {
        if(client.checkEngineHealth() && k8sClient.k8sCanListNamespaces() &&
                k8sClient.k8sCanListDeployments() && k8sClient.k8sCanListDaemonSets() && k8sClient.k8sCanListStatefulSets()) {
            return "Healthy!";
        }
        log.warn("Healthcheck failed");
        throw new ServerNotHealthyException();
    }

    @GetMapping("/aye")
    public String aye() {
        return "Welcome to the Aye Server! Happy scanning!";
    }

}
