
package com.furioussoulk.apm.collector.core.exception;

public class CycleDependencyException extends RuntimeException {
    public CycleDependencyException(String message) {
        super(message);
    }
}
