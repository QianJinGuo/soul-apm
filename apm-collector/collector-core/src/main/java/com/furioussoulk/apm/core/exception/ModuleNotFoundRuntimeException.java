
package com.furioussoulk.apm.core.exception;

public class ModuleNotFoundRuntimeException extends RuntimeException {

    public ModuleNotFoundRuntimeException(Throwable cause) {
        super(cause);
    }

    public ModuleNotFoundRuntimeException(String message) {
        super(message);
    }
}
