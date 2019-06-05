package com.furioussoulk.collector.agent.jetty.naming;

import com.furioussoulk.collector.agent.jetty.AgentJettyModule;
import com.furioussoulk.collector.agent.jetty.AgentModuleJettyProvider;
import com.furioussoulk.collector.cluster.ClusterModuleListener;

public class AgentJettyNamingListener extends ClusterModuleListener {

    public static final String PATH = "/" + AgentJettyModule.NAME + "/" + AgentModuleJettyProvider.NAME;

    @Override public String path() {
        return PATH;
    }

    @Override public void serverJoinNotify(String serverAddress) {

    }

    @Override public void serverQuitNotify(String serverAddress) {

    }
}
