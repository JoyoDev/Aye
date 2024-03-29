package com.joyodev.aye.server;

import com.joyodev.aye.operator.ClusterImageOperator;
import com.joyodev.aye.operator.Operator;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ServerLoop {

    private K8sClient k8sClient;
    private Operator operator;

    public ServerLoop(MeterRegistry meterRegistry) throws IOException {
        k8sClient  = K8sClient.getInstance();
        operator = new ClusterImageOperator(meterRegistry);
    }

    @Scheduled(fixedDelayString = "${service.delay.in.milliseconds}")
    public void loop() {
        List<String> clusterImages = k8sClient.getClusterImages();

        operator.setCurrentImages(clusterImages);
        operator.operate();
    }
}
