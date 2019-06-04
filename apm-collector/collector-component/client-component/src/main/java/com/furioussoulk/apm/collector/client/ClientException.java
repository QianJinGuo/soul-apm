package com.furioussoulk.apm.collector.client;

import com.furioussoulk.apm.core.exception.CollectorException;

public abstract class ClientException extends CollectorException {
    public ClientException(String message) {
        super(message);
    }

    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
