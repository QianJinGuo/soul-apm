package com.furioussoulk.client;

import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.exception.CollectorException;

public abstract class ClientException extends CollectorException {
    public ClientException(String message) {
        super(message);
    }

    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
