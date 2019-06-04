package com.furioussoulk.apm.collector.core.collector.server.grpc;

import com.furioussoulk.apm.collector.core.collector.server.ServerException;

public class GRPCServerException extends ServerException {

    public GRPCServerException(String message) {
        super(message);
    }

    public GRPCServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
