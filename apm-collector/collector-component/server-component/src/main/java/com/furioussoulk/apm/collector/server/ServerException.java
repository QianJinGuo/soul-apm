package com.furioussoulk.apm.collector.server;

import com.furioussoulk.apm.core.exception.CollectorException;

public abstract class ServerException extends CollectorException {

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
