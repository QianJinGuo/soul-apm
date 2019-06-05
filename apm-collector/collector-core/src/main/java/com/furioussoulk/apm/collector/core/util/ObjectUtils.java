package com.furioussoulk.apm.collector.core.util;

public class ObjectUtils {
    public static boolean isEmpty(Object obj) {
        return obj == null;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }
}
