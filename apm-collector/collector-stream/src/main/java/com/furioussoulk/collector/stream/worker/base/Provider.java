package com.furioussoulk.collector.stream.worker.base;

import com.furioussoulk.collector.stream.exception.ProviderNotFoundException;

public interface Provider {

    WorkerRef create(WorkerCreateListener workerCreateListener) throws ProviderNotFoundException;
}
