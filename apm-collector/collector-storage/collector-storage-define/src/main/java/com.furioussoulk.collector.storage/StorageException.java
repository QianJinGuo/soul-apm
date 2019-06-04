package com.furioussoulk.collector.storage;

import com.furioussoulk.apm.core.exception.CollectorException;

public abstract class StorageException extends CollectorException {

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
