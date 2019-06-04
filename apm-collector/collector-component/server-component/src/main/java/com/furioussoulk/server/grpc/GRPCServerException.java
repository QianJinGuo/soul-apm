package com.furioussoulk.server.grpc;

import com.furioussoulk.server.ServerException;

public class GRPCServerException extends ServerException {

    public GRPCServerException(String message) {
        super(message);
    }

    public GRPCServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
