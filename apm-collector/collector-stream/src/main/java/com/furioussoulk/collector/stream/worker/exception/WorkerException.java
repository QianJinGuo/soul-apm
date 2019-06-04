package com.furioussoulk.collector.stream.worker.exception;


import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.exception.CollectorException;

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
