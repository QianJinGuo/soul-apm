package com.furioussoulk.agent.stream.service;

import com.furioussoulk.agent.stream.service.jvm.*;
import com.furioussoulk.agent.stream.service.register.IApplicationIDService;
import com.furioussoulk.agent.stream.service.register.IInstanceIDService;
import com.furioussoulk.agent.stream.service.register.IServiceNameService;
import com.furioussoulk.agent.stream.service.trace.ITraceSegmentService;
import com.furioussoulk.apm.collector.core.module.Module;

import java.util.ArrayList;
import java.util.List;

public class AgentStreamModule extends Module {

    public static final String NAME = "agent_stream";

    @Override public String name() {
        return NAME;
    }

    @Override public Class[] services() {
        List<Class> classes = new ArrayList<>();

        addRegisterService(classes);
        addJVMService(classes);
        classes.add(ITraceSegmentService.class);

        return classes.toArray(new Class[] {});
    }

    private void addRegisterService(List<Class> classes) {
        classes.add(IApplicationIDService.class);
        classes.add(IInstanceIDService.class);
        classes.add(IServiceNameService.class);
    }

    private void addJVMService(List<Class> classes) {
        classes.add(ICpuMetricService.class);
        classes.add(IGCMetricService.class);
        classes.add(IMemoryMetricService.class);
        classes.add(IMemoryPoolMetricService.class);
        classes.add(IInstanceHeartBeatService.class);
    }
}
