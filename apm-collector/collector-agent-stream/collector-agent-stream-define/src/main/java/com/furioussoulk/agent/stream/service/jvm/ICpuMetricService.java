package com.furioussoulk.agent.stream.service.jvm;

import com.furioussoulk.apm.collector.core.module.Service;

public interface ICpuMetricService extends Service {
    void send(int instanceId, long timeBucket, double usagePercent);
}
