package com.furioussoulk.agent.stream.service.jvm;

import com.furioussoulk.apm.collector.core.module.Service;

public interface IGCMetricService extends Service {
    void send(int instanceId, long timeBucket, int phraseValue, long count, long time);
}
