package com.furioussoulk.apm.collector.server.jetty;

import com.furioussoulk.apm.collector.core.exception.CollectorException;

public class ArgumentsParseException extends CollectorException {

    public ArgumentsParseException(String message) {
        super(message);
    }

    public ArgumentsParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
