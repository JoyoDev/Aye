package com.joyodev.aye.server;

import com.joyodev.aye.operator.ClusterImageOperator;
import com.joyodev.aye.operator.Operator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ServerLoop {

    K8sClient k8sClient = K8sClient.getInstance();
    Operator operator;

    public ServerLoop() throws IOException {
        operator = new ClusterImageOperator();
    }

    @Scheduled(fixedRate = 5000)
    public void loop() {
        List<String> clusterImages = k8sClient.getClusterImages();
        operator.setCurrentImages(clusterImages);
        operator.operate();
    }
}
