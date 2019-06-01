package com.furioussoulk.logger.api;

public interface ILoggerFactory {
    ILogger getLogger(Class<?> clazz);
}
