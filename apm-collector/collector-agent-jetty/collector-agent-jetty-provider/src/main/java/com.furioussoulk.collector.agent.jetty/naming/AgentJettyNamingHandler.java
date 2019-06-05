package com.furioussoulk.collector.agent.jetty.naming;

import com.furioussoulk.apm.collector.server.jetty.ArgumentsParseException;
import com.furioussoulk.apm.collector.server.jetty.JettyHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public class AgentJettyNamingHandler extends JettyHandler {

    private final AgentJettyNamingListener namingListener;

    public AgentJettyNamingHandler(AgentJettyNamingListener namingListener) {
        this.namingListener = namingListener;
    }

    @Override public String pathSpec() {
        return "/agent/jetty";
    }

    @Override protected JsonElement doGet(HttpServletRequest req) throws ArgumentsParseException {
        Set<String> servers = namingListener.getAddresses();
        JsonArray serverArray = new JsonArray();
        servers.forEach(serverArray::add);
        return serverArray;
    }

    @Override protected JsonElement doPost(HttpServletRequest req) throws ArgumentsParseException {
        throw new UnsupportedOperationException();
    }
}
