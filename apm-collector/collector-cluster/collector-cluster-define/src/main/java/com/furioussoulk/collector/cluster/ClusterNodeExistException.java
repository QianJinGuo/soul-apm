package com.furioussoulk.collector.cluster;

public class ClusterNodeExistException extends ClusterException {

    public ClusterNodeExistException(String message) {
        super(message);
    }

    public ClusterNodeExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
