package com.furioussoulk.apm.collector.core.collector.server;

import com.furioussoulk.apm.collector.core.exception.CollectorException;

public abstract class ServerException extends CollectorException {

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
