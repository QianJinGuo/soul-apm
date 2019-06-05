package com.furioussoulk.agent.stream.service.jvm;

import com.furioussoulk.apm.collector.core.module.Service;

public interface IMemoryPoolMetricService extends Service {
    void send(int instanceId, long timeBucket, int poolType, long init, long max, long used, long commited);
}
