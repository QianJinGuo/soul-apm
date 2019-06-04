package com.furioussoulk.apm.core.logger.api;

public interface ILogger {

    void info(String format);

    void info(String format, Object... arguments);

    void warn(String format, Object... arguments);

    void error(String format, Throwable e);

    void error(Throwable e, String format, Object... arguments);

    boolean isDebugEnable();

    boolean isInfoEnable();

    boolean isWarnEnable();

    boolean isErrorEnable();

    void debug(String format);

    void debug(String format, Object... arguments);

    void error(String format);
}
