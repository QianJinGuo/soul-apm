package com.furioussoulk.collector.boot.config;

import com.furioussoulk.apm.collector.core.exception.CollectorException;

public class ConfigFileNotFoundException extends CollectorException {

    public ConfigFileNotFoundException(String message) {
        super(message);
    }

    public ConfigFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
