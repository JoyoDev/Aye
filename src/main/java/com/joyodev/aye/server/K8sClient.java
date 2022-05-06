package com.joyodev.aye.server;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.util.Config;

import java.io.IOException;

public final class K8sClient {

    public static K8sClient INSTANCE;
    //private ApiClient apiClient = Config.defaultClient();

    public K8sClient() throws IOException {
    }

    public static K8sClient getInstance() throws IOException {
        if(INSTANCE == null) {
            INSTANCE = new K8sClient();
        }
        return INSTANCE;
    }

}
