package com.furioussoulk.apm.collector.core.cache;

public interface Collection<Data> {

    void reading();

    boolean isReading();

    void writing();

    boolean isWriting();

    void clear();

    int size();

    void finishReading();

    void finishWriting();

    Data collection();
}
