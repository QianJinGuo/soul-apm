
package com.furioussoulk.collector.stream.worker.collector.stream.worker.core.exception;

public class CycleDependencyException extends RuntimeException {
    public CycleDependencyException(String message) {
        super(message);
    }
}
