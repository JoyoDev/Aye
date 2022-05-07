package com.joyodev.aye.server;

import com.joyodev.aye.operator.ClusterImageOperator;
import com.joyodev.aye.operator.Operator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ServerLoop {

    K8sClient k8sClient;
    Operator operator;

    public ServerLoop() throws IOException {
        k8sClient  = K8sClient.getInstance();
        operator = new ClusterImageOperator();
    }

    @Scheduled(fixedRate = 5000)
    public void loop() {
        List<String> clusterImages = k8sClient.getClusterImages();
        List<String> list = new ArrayList<>();
        list.add("docker.io/library/nginx");
        list.add("docker.io/library/httpd");
        list.add("docker.io/library/tomcat");
        list.add("alexandar12/amop-dev:5");

        operator.setCurrentImages(list);
        operator.operate();
    }
}
