package com.furioussoulk.collector.stream.exception;

/**
 * This exception is raised when worker fails to process job during "call" or "ask"
 */
public class WorkerInvokeException extends WorkerException {

    public WorkerInvokeException(String message) {
        super(message);
    }

    public WorkerInvokeException(String message, Throwable cause) {
        super(message, cause);
    }
}
