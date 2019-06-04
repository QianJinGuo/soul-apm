package com.furioussoulk.server;

import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.exception.CollectorException;

public abstract class ServerException extends CollectorException {

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
