package com.furioussoulk.collector.stream.worker.collector.stream.worker.core.exception;

/**
 *
 * @author 孙证杰
 * @date 2019/6/3 13:24
 */
public class CollectorException extends Exception{
    public CollectorException(String message) {
        super(message);
    }

    public CollectorException(String message, Throwable cause) {
        super(message, cause);
    }
}
