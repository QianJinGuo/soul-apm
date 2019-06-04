package com.furioussoulk.collector.stream.worker.collector.stream.worker.core.define;

import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.exception.DefineException;

public interface Loader<T> {
    T load() throws DefineException;
}
