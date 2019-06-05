package com.furioussoulk.apm.collector.server.jetty;

import com.furioussoulk.apm.collector.server.ServerException;

public class JettyServerException extends ServerException {

    public JettyServerException(String message) {
        super(message);
    }

    public JettyServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
