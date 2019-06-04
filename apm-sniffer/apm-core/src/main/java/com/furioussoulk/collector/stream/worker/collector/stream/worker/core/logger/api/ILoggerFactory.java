package com.furioussoulk.collector.stream.worker.collector.stream.worker.core.logger.api;

public interface ILoggerFactory {
    ILogger getLogger(Class<?> clazz);
}
