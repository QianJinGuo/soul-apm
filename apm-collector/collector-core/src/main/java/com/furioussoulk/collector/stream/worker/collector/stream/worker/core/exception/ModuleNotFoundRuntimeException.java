
package com.furioussoulk.collector.stream.worker.collector.stream.worker.core.exception;

public class ModuleNotFoundRuntimeException extends RuntimeException {

    public ModuleNotFoundRuntimeException(Throwable cause) {
        super(cause);
    }

    public ModuleNotFoundRuntimeException(String message) {
        super(message);
    }
}
