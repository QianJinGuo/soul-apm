package com.furioussoulk.logger;

import com.furioussoulk.logger.api.ILogger;
import com.furioussoulk.logger.api.ILoggerFactory;

public class LoggerFactory implements ILoggerFactory {
    @Override
    public ILogger getLogger(Class<?> clazz) {
        return new Logger(clazz);
    }
}
