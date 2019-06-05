package com.furioussoulk.collector.agent.grpc.handler.naming;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.Set;

public class AgentGRPCNamingHandler {

    private final AgentGRPCNamingListener namingListener;

    public AgentGRPCNamingHandler(AgentGRPCNamingListener namingListener) {
        this.namingListener = namingListener;
    }

    @Override public String pathSpec() {
        return "/agent/gRPC";
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
