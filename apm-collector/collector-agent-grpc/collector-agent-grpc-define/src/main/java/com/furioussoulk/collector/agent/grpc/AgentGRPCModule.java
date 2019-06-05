package com.furioussoulk.collector.agent.grpc;

import com.furioussoulk.apm.collector.core.module.Module;

public class AgentGRPCModule extends Module {

    public static final String NAME = "agent_gRPC";

    @Override public String name() {
        return NAME;
    }

    @Override public Class[] services() {
        return new Class[0];
    }
}
