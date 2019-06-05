package com.furioussoulk.apm.common.datacarrier.consumer;

public class ConsumerCannotBeCreatedException extends RuntimeException {
    ConsumerCannotBeCreatedException(Throwable t) {
        super(t);
    }
}
