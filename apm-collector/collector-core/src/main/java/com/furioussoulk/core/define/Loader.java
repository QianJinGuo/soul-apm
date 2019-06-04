package com.furioussoulk.core.define;

import com.furioussoulk.core.exception.DefineException;

public interface Loader<T> {
    T load() throws DefineException;
}
