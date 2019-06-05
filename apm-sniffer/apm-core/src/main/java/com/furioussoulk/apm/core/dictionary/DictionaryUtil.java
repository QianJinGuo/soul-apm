package com.furioussoulk.apm.core.dictionary;

public class DictionaryUtil {
    public static int nullValue() {
        return 0;
    }

    public static boolean isNull(int id) {
        return id == nullValue();
    }
}
