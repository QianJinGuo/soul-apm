package com.furioussoulk.apm.common.datacarrier.consumer;

import java.util.List;

public interface IConsumer<T> {
    void init();

    void consume(List<T> data);

    void onError(List<T> data, Throwable t);

    void onExit();
}
