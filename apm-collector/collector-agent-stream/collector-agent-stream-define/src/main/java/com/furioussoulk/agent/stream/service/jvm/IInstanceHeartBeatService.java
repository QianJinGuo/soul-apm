package com.furioussoulk.agent.stream.service.jvm;

import com.furioussoulk.apm.collector.core.module.Service;

public interface IInstanceHeartBeatService extends Service {
    void send(int instanceId, long heartBeatTime);
}
