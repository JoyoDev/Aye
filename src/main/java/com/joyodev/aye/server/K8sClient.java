package com.joyodev.aye.server;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Config;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.util.*;

@Slf4j
public final class K8sClient {

    private static K8sClient INSTANCE;
    private final ApiClient apiClient = Config.defaultClient();

    private K8sClient() throws IOException {
    }

    public static K8sClient getInstance() throws IOException {
        if(INSTANCE == null) {
            INSTANCE = new K8sClient();
        }
        return INSTANCE;
    }

    public boolean k8sCanListNamespaces() {
        try {
            CoreV1Api api = new CoreV1Api(apiClient);
            V1NamespaceList namespaceList = api.listNamespace(null, null, null, null, null,
                    null, null, null, 10, false);
            log.debug(String.format("Namespaces: %s", namespaceList.toString()));
            return true;
        } catch (ApiException e) {
            log.error("Could not list Namespaces", e);
            return false;
        }
    }

    public boolean k8sCanListDeployments() {
        try {
            AppsV1Api api = new AppsV1Api(apiClient);
            V1DeploymentList deploymentList = api.listNamespacedDeployment("default",null, null, null,
                    null, null, null, null, null, 10, false);
            log.debug(String.format("Deployments in default namespace: %s", deploymentList.toString()));
            return true;
        } catch (ApiException e) {
            log.error("Could not list Deployments", e);
            return false;
        }
    }

    public boolean k8sCanListDaemonSets() {
        try {
            AppsV1Api api = new AppsV1Api(apiClient);
            V1DaemonSetList daemonSetList = api.listNamespacedDaemonSet("default",null, null, null,
                    null, null, null, null, null, 10, false);
            log.debug(String.format("DaemonSets in default namespace: %s", daemonSetList.toString()));
            return true;
        } catch (ApiException e) {
            log.error("Could not list DaemonSets", e);
            return false;
        }
    }

    public boolean k8sCanListStatefulSets() {
        try {
            AppsV1Api api = new AppsV1Api(apiClient);
            V1StatefulSetList statefulSetList = api.listNamespacedStatefulSet("default",null, null,
                    null, null, null, null, null, null, 10, false);
            log.debug(String.format("StatefulSets in default namespace: %s", statefulSetList.toString()));
            return true;
        } catch (ApiException e) {
            log.error("Could not list StatefulSets", e);
            return false;
        }
    }

    public ArrayList<String> getClusterImages() {
        Map<String, Boolean> uniqueImages = new HashMap<>();
        ArrayList<String> images = new ArrayList<>();

        try {
            CoreV1Api api = new CoreV1Api(apiClient);
            V1PodList podList = api.listPodForAllNamespaces(null, null, null, null, null,
                    null, null, null, null, null);
            List<V1Pod> pods = podList.getItems();

            for(V1Pod pod : pods) {
                for(V1Container container : Objects.requireNonNull(pod.getSpec()).getContainers()) {
                    if(!uniqueImages.containsKey(container.getImage())) {
                        uniqueImages.put(container.getImage(), true);
                        images.add(container.getImage());
                    }
                }
            }

        } catch (ApiException e) {
            log.error("Could not list Pods", e);
        }

        return images;
    }

}
