package com.furioussoulk.apm.core.exception;

public class ConfigNotFoundException extends Exception {
    public ConfigNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigNotFoundException(String message) {
        super(message);
    }
}
