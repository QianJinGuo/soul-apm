

package com.furioussoulk.collector.stream.worker.collector.stream.worker.core.logger;

import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.logger.api.ILogger;


public class LogManager {
    private static LoggerFactory LOGGER_FACTORY = new LoggerFactory();

    public static void setLogResolver(LoggerFactory factory) {
        LogManager.LOGGER_FACTORY = factory;
    }

    public static ILogger getLogger(Class<?> clazz) {
        if (LOGGER_FACTORY == null) {
            return NoopLogger.INSTANCE;
        }
        return LogManager.LOGGER_FACTORY.getLogger(clazz);
    }
}
