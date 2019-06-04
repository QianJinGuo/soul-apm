package com.furioussoulk.collector.stream.worker.collector.stream.worker.core.exception;

/**
 * @author peng-yongsheng
 */
public abstract class DefineException extends CollectorException {

    public DefineException(String message) {
        super(message);
    }

    public DefineException(String message, Throwable cause) {
        super(message, cause);
    }
}
