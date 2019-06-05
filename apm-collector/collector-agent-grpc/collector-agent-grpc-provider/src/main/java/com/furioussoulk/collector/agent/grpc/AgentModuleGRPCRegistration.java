package com.furioussoulk.collector.agent.grpc;

public class AgentModuleGRPCRegistration extends ModuleRegistration {

    private final String host;
    private final int port;

    public AgentModuleGRPCRegistration(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override public Value buildValue() {
        return new Value(host, port, Const.EMPTY_STRING);
    }
}
