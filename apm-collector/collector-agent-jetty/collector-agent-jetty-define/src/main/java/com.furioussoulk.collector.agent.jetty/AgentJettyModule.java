package com.furioussoulk.collector.agent.jetty;

import com.furioussoulk.apm.collector.core.module.Module;

public class AgentJettyModule extends Module {

    public static final String NAME = "agent_jetty";

    @Override public String name() {
        return NAME;
    }

    @Override public Class[] services() {
        return new Class[0];
    }
}
