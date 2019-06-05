package com.furioussoulk.agent.stream.service.jvm;

import com.furioussoulk.apm.collector.core.module.Service;

public interface IMemoryMetricService extends Service {
    void send(int instanceId, long timeBucket, boolean isHeap, long init, long max, long used, long commited);
}
