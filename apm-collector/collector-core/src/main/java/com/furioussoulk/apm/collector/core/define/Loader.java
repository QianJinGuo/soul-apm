package com.furioussoulk.apm.collector.core.define;

import com.furioussoulk.apm.core.exception.DefineException;

public interface Loader<T> {
    T load() throws DefineException;
}
