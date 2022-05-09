package com.joyodev.aye.server;

import com.joyodev.aye.operator.ClusterImageOperator;
import com.joyodev.aye.operator.Operator;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ServerLoop {

    K8sClient k8sClient;
    Operator operator;

    public ServerLoop(MeterRegistry meterRegistry) throws IOException {
        k8sClient  = K8sClient.getInstance();
        operator = new ClusterImageOperator(meterRegistry);
    }

    @Scheduled(fixedRate = 5000)
    public void loop() {
        //List<String> clusterImages = k8sClient.getClusterImages();
        List<String> clusterImages = new ArrayList<>();
        clusterImages.add("docker.io/library/debian");
        clusterImages.add("docker.io/library/nginx");
        clusterImages.add("docker.io/library/tomcat");
        operator.setCurrentImages(clusterImages);
        operator.operate();
    }
}
