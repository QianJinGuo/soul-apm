package com.furioussoulk.agent.stream.service.register;


import com.furioussoulk.apm.collector.core.module.Service;

public interface IInstanceIDService extends Service {
    int getOrCreate(int applicationId, String agentUUID, long registerTime, String osInfo);

    void recover(int instanceId, int applicationId, long registerTime, String osInfo);
}
