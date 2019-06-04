package com.furioussoulk.apm.collector.core.core.logger.api;

public interface ILoggerFactory {
    ILogger getLogger(Class<?> clazz);
}
