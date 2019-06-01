package com.furioussoulk.logger;

import com.furioussoulk.conf.Config;
import com.furioussoulk.conf.Constants;
import com.furioussoulk.logger.api.ILogger;
import com.furioussoulk.util.StringUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;

/**
 * 日志
 *
 * @author 孙证杰-YCKJ1697
 * @email sunzhengjie@cloudwalk.cn
 * @date 2019/5/31 13:01
 */
public class Logger implements ILogger {

    private Class targetClass;

    public Logger(Class targetClass) {
        this.targetClass = targetClass;
    }

    protected void logger(LogLevel level, String message, Throwable e) {
        WriterFactory.getLogWriter().write(format(level, message, e));
    }

    private String replaceParam(String message, Object... parameters) {
        int startSize = 0;
        int parametersIndex = 0;
        int index;
        String tmpMessage = message;
        while ((index = message.indexOf("{}", startSize)) != -1) {
            if (parametersIndex >= parameters.length) {
                break;
            }
            /**
             * @Fix the Illegal group reference issue
             */
            tmpMessage = tmpMessage.replaceFirst("\\{\\}", Matcher.quoteReplacement(String.valueOf(parameters[parametersIndex++])));
            startSize = index + 2;
        }
        return tmpMessage;
    }

    String format(LogLevel level, String message, Throwable t) {
        return StringUtil.join(' ', level.name(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                targetClass.getSimpleName(),
                ": ",
                message,
                t == null ? "" : format(t)
        );
    }

    String format(Throwable t) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        t.printStackTrace(new java.io.PrintWriter(buf, true));
        String expMessage = buf.toString();
        try {
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Constants.LINE_SEPARATOR + expMessage;
    }

    @Override
    public void info(String format) {
        if (isInfoEnable())
            logger(LogLevel.INFO, format, null);
    }

    @Override
    public void info(String format, Object... arguments) {
        if (isInfoEnable())
            logger(LogLevel.INFO, replaceParam(format, arguments), null);
    }

    @Override
    public void warn(String format, Object... arguments) {
        if (isWarnEnable())
            logger(LogLevel.WARN, replaceParam(format, arguments), null);
    }

    @Override
    public void error(String format, Throwable e) {
        if (isErrorEnable())
            logger(LogLevel.ERROR, format, e);
    }

    @Override
    public void error(Throwable e, String format, Object... arguments) {
        if (isErrorEnable())
            logger(LogLevel.ERROR, replaceParam(format, arguments), e);
    }

    @Override
    public boolean isDebugEnable() {
        return LogLevel.DEBUG.compareTo(Config.Logging.LEVEL) >= 0;
    }

    @Override
    public boolean isInfoEnable() {
        return LogLevel.INFO.compareTo(Config.Logging.LEVEL) >= 0;
    }

    @Override
    public boolean isWarnEnable() {
        return LogLevel.WARN.compareTo(Config.Logging.LEVEL) >= 0;
    }

    @Override
    public boolean isErrorEnable() {
        return LogLevel.ERROR.compareTo(Config.Logging.LEVEL) >= 0;
    }

    @Override
    public void debug(String format) {
        if (isDebugEnable()) {
            logger(LogLevel.DEBUG, format, null);
        }
    }

    @Override
    public void debug(String format, Object... arguments) {
        if (isDebugEnable()) {
            logger(LogLevel.DEBUG, replaceParam(format, arguments), null);
        }
    }

    @Override
    public void error(String format) {
        if (isErrorEnable()) {
            logger(LogLevel.ERROR, format, null);
        }
    }
}
