package com.furioussoulk.apm.core.logger.api;

public interface ILoggerFactory {
    ILogger getLogger(Class<?> clazz);
}
