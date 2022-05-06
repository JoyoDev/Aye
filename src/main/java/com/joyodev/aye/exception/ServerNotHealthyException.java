package com.joyodev.aye.exception;

public class ServerNotHealthyException extends RuntimeException {
    public ServerNotHealthyException() {
        super("Server is not healthy. Please check the logs to see whether Anchore Engine is unreachable or there is an issue with Kubernetes Cluster.");
    }
}
