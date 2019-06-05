package com.furioussoulk.collector.cluster;

import com.furioussoulk.apm.collector.core.exception.CollectorException;

public abstract class ClusterException extends CollectorException {

    public ClusterException(String message) {
        super(message);
    }

    public ClusterException(String message, Throwable cause) {
        super(message, cause);
    }
}
