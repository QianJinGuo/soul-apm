package com.furioussoulk.collector.stream.worker.collector.stream.worker.core.logger;

import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.logger.api.ILogger;
import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.logger.api.ILoggerFactory;

public class LoggerFactory implements ILoggerFactory {
    @Override
    public ILogger getLogger(Class<?> clazz) {
        return new Logger(clazz);
    }
}
