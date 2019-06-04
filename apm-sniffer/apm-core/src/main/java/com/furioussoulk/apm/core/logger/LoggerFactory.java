package com.furioussoulk.apm.core.logger;

import com.furioussoulk.apm.core.logger.api.ILogger;
import com.furioussoulk.apm.core.logger.api.ILoggerFactory;

public class LoggerFactory implements ILoggerFactory {
    @Override
    public ILogger getLogger(Class<?> clazz) {
        return new Logger(clazz);
    }
}
