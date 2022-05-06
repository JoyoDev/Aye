package com.joyodev.aye.server;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1DaemonSetList;
import io.kubernetes.client.openapi.models.V1DeploymentList;
import io.kubernetes.client.openapi.models.V1NamespaceList;
import io.kubernetes.client.openapi.models.V1StatefulSetList;
import io.kubernetes.client.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class K8sClient {

    private static K8sClient INSTANCE;
    private ApiClient apiClient = Config.defaultClient();

    Logger logger = LoggerFactory.getLogger(K8sClient.class);

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
            V1NamespaceList namespaceList = api.listNamespace(null, null, null, null, null, null, null, null, 10, false);
            logger.info(String.format("Namespaces: %s", String.valueOf(namespaceList.toString())));
            return true;
        } catch (ApiException e) {
            e.printStackTrace();
            logger.error("Could not list Namespaces");
            return false;
        }
    }

    public boolean k8sCanListDeployments() {
        try {
            AppsV1Api api = new AppsV1Api(apiClient);
            V1DeploymentList deploymentList = api.listNamespacedDeployment("default",null, null, null, null, null, null, null, null, 10, false);
            logger.info(String.format("Deployments in default namespace: %s", String.valueOf(deploymentList.toString())));
            return true;
        } catch (ApiException e) {
            e.printStackTrace();
            logger.error("Could not list Deployments");
            return false;
        }
    }

    public boolean k8sCanListDaemonSets() {
        try {
            AppsV1Api api = new AppsV1Api(apiClient);
            V1DaemonSetList daemonSetList = api.listNamespacedDaemonSet("default",null, null, null, null, null, null, null, null, 10, false);
            logger.info(String.format("DaemonSets in default namespace: %s", String.valueOf(daemonSetList.toString())));
            return true;
        } catch (ApiException e) {
            e.printStackTrace();
            logger.error("Could not list DaemonSets");
            return false;
        }
    }

    public boolean k8sCanListStatefulSets() {
        try {
            AppsV1Api api = new AppsV1Api(apiClient);
            V1StatefulSetList statefulSetList = api.listNamespacedStatefulSet("default",null, null, null, null, null, null, null, null, 10, false);
            logger.info(String.format("StatefulSets in default namespace: %s", String.valueOf(statefulSetList.toString())));
            return true;
        } catch (ApiException e) {
            e.printStackTrace();
            logger.error("Could not list StatefulSets");
            return false;
        }
    }

}
