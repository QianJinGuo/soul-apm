package com.furioussoulk.apm.collector.core.collector.client;

import com.furioussoulk.apm.collector.core.exception.CollectorException;

public abstract class ClientException extends CollectorException {
    public ClientException(String message) {
        super(message);
    }

    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
