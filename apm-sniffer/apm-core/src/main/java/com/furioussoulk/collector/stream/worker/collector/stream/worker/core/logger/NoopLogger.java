

package com.furioussoulk.collector.stream.worker.collector.stream.worker.core.logger;

import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.logger.api.ILogger;

public enum NoopLogger implements ILogger {
    INSTANCE {

    };

    @Override
    public void info(String message) {

    }

    @Override
    public void info(String format, Object... arguments) {

    }

    @Override
    public void warn(String format, Object... arguments) {

    }

    @Override
    public void error(String format, Throwable e) {

    }

    @Override
    public boolean isDebugEnable() {
        return false;
    }

    @Override
    public boolean isInfoEnable() {
        return false;
    }

    @Override
    public boolean isWarnEnable() {
        return false;
    }

    @Override
    public boolean isErrorEnable() {
        return false;
    }

    @Override
    public void debug(String format) {

    }

    @Override
    public void debug(String format, Object... arguments) {

    }

    @Override
    public void error(String format) {

    }

    @Override
    public void error(Throwable e, String format, Object... arguments) {

    }
}
