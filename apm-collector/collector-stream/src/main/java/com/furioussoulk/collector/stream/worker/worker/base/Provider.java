package com.furioussoulk.collector.stream.worker.worker.base;

import com.furioussoulk.collector.stream.worker.exception.ProviderNotFoundException;

public interface Provider {

    WorkerRef create(WorkerCreateListener workerCreateListener) throws ProviderNotFoundException;
}
