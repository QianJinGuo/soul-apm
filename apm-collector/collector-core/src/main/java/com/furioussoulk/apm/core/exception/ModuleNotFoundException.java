
package com.furioussoulk.apm.core.exception;

public class ModuleNotFoundException extends Exception {

    public ModuleNotFoundException(Throwable cause) {
        super(cause);
    }

    public ModuleNotFoundException(String message) {
        super(message);
    }
}
