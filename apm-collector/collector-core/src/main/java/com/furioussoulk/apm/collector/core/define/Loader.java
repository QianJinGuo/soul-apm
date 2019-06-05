package com.furioussoulk.apm.collector.core.define;

import com.furioussoulk.apm.collector.core.exception.DefineException;

public interface Loader<T> {
    T load() throws DefineException;
}
