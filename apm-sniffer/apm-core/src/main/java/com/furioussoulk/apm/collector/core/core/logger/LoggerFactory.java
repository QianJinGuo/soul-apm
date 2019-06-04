package com.furioussoulk.apm.collector.core.core.logger;

import com.furioussoulk.apm.collector.core.core.logger.api.ILogger;
import com.furioussoulk.apm.collector.core.core.logger.api.ILoggerFactory;

public class LoggerFactory implements ILoggerFactory {
    @Override
    public ILogger getLogger(Class<?> clazz) {
        return new Logger(clazz);
    }
}
