package com.furioussoulk.collector.agent.grpc;

import com.furioussoulk.apm.collector.core.module.Module;
import com.furioussoulk.apm.collector.core.module.ModuleProvider;
import com.furioussoulk.apm.collector.core.exception.ServiceNotProvidedException;

import java.util.Properties;

public class AgentModuleGRPCProvider extends ModuleProvider {

    public static final String NAME = "gRPC";
    private static final String HOST = "host";
    private static final String PORT = "port";

    @Override public String name() {
        return NAME;
    }

    @Override public Class<? extends Module> module() {
        return AgentGRPCModule.class;
    }

    @Override public void prepare(Properties config) throws ServiceNotProvidedException {

    }

    @Override public void start(Properties config) throws ServiceNotProvidedException {
        String host = config.getProperty(HOST);
        Integer port = (Integer)config.get(PORT);

        ModuleRegisterService moduleRegisterService = getManager().find(ClusterModule.NAME).getService(ModuleRegisterService.class);
        moduleRegisterService.register(AgentGRPCModule.NAME, this.name(), new AgentModuleGRPCRegistration(host, port));

        AgentGRPCNamingListener namingListener = new AgentGRPCNamingListener();
        ModuleListenerService moduleListenerService = getManager().find(ClusterModule.NAME).getService(ModuleListenerService.class);
        moduleListenerService.addListener(namingListener);

        NamingHandlerRegisterService namingHandlerRegisterService = getManager().find(NamingModule.NAME).getService(NamingHandlerRegisterService.class);
        namingHandlerRegisterService.register(new AgentGRPCNamingHandler(namingListener));

        GRPCManagerService managerService = getManager().find(GRPCManagerModule.NAME).getService(GRPCManagerService.class);
        Server gRPCServer = managerService.createIfAbsent(host, port);

        addHandlers(gRPCServer);
    }

    @Override public void notifyAfterCompleted() throws ServiceNotProvidedException {

    }

    @Override public String[] requiredModules() {
        return new String[] {ClusterModule.NAME, NamingModule.NAME, GRPCManagerModule.NAME, AgentStreamModule.NAME};
    }

    private void addHandlers(Server gRPCServer) {
        gRPCServer.addHandler(new ApplicationRegisterServiceHandler(getManager()));
        gRPCServer.addHandler(new InstanceDiscoveryServiceHandler(getManager()));
        gRPCServer.addHandler(new ServiceNameDiscoveryServiceHandler(getManager()));
        gRPCServer.addHandler(new JVMMetricsServiceHandler(getManager()));
        gRPCServer.addHandler(new TraceSegmentServiceHandler(getManager()));
    }
}
