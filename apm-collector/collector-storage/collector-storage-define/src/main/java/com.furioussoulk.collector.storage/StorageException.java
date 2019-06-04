package com.furioussoulk.collector.storage;

import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.exception.CollectorException;

public abstract class StorageException extends CollectorException {

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
