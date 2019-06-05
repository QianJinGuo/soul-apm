package com.furioussoulk.apm.collector.core.util;

public class StringUtils {

    public static final String EMPTY_STRING = "";

    public static boolean isEmpty(Object str) {
        return str == null || EMPTY_STRING.equals(str);
    }

    public static boolean isNotEmpty(Object str) {
        return !isEmpty(str);
    }
}
