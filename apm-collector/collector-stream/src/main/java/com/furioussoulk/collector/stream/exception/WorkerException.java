package com.furioussoulk.collector.stream.exception;


import com.furioussoulk.apm.collector.core.exception.CollectorException;

/**
 * Defines a general exception a worker can throw when it
 * encounters difficulty.
 */
public class WorkerException extends CollectorException {

    public WorkerException(String message) {
        super(message);
    }

    public WorkerException(String message, Throwable cause) {
        super(message, cause);
    }
}
