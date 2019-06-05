package com.furioussoulk.collector.agent.jetty;

import com.furioussoulk.apm.collector.core.exception.ServiceNotProvidedException;
import com.furioussoulk.apm.collector.core.module.Module;
import com.furioussoulk.apm.collector.core.module.ModuleProvider;
import com.furioussoulk.collector.agent.jetty.naming.AgentJettyNamingHandler;
import com.furioussoulk.collector.agent.jetty.naming.AgentJettyNamingListener;
import com.furioussoulk.collector.cluster.ClusterModule;
import com.furioussoulk.collector.cluster.service.ModuleListenerService;
import com.furioussoulk.collector.cluster.service.ModuleRegisterService;

import java.util.Properties;

public class AgentModuleJettyProvider extends ModuleProvider {

    public static final String NAME = "jetty";
    private static final String HOST = "host";
    private static final String PORT = "port";
    private static final String CONTEXT_PATH = "context_path";

    @Override public String name() {
        return NAME;
    }

    @Override public Class<? extends Module> module() {
        return AgentJettyModule.class;
    }

    @Override public void prepare(Properties config) throws ServiceNotProvidedException {

    }

    @Override public void start(Properties config) throws ServiceNotProvidedException {
        String host = config.getProperty(HOST);
        Integer port = (Integer)config.get(PORT);
        String contextPath = config.getProperty(CONTEXT_PATH);

        ModuleRegisterService moduleRegisterService = getManager().find(ClusterModule.NAME).getService(ModuleRegisterService.class);
        moduleRegisterService.register(AgentJettyModule.NAME, this.name(), new AgentModuleJettyRegistration(host, port, contextPath));

        AgentJettyNamingListener namingListener = new AgentJettyNamingListener();
        ModuleListenerService moduleListenerService = getManager().find(ClusterModule.NAME).getService(ModuleListenerService.class);
        moduleListenerService.addListener(namingListener);

        NamingHandlerRegisterService namingHandlerRegisterService = getManager().find(NamingModule.NAME).getService(NamingHandlerRegisterService.class);
        namingHandlerRegisterService.register(new AgentJettyNamingHandler(namingListener));

        JettyManagerService managerService = getManager().find(JettyManagerModule.NAME).getService(JettyManagerService.class);
        Server jettyServer = managerService.createIfAbsent(host, port, contextPath);
        addHandlers(jettyServer);
    }

    @Override public void notifyAfterCompleted() throws ServiceNotProvidedException {

    }

    @Override public String[] requiredModules() {
        return new String[] {ClusterModule.NAME, NamingModule.NAME, JettyManagerModule.NAME, AgentStreamModule.NAME};
    }

    private void addHandlers(Server jettyServer) {
        jettyServer.addHandler(new TraceSegmentServletHandler(getManager()));
        jettyServer.addHandler(new ApplicationRegisterServletHandler(getManager()));
        jettyServer.addHandler(new InstanceDiscoveryServletHandler(getManager()));
        jettyServer.addHandler(new ServiceNameDiscoveryServiceHandler(getManager()));
    }
}
