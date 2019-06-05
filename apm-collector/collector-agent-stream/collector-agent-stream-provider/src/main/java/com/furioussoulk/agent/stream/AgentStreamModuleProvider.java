package com.furioussoulk.agent.stream;

import com.furioussoulk.agent.stream.buffer.BufferFileConfig;
import com.furioussoulk.agent.stream.worker.AgentStreamRemoteDataRegister;
import com.furioussoulk.agent.stream.worker.jvm.*;
import com.furioussoulk.agent.stream.worker.register.ApplicationIDService;
import com.furioussoulk.agent.stream.worker.register.InstanceIDService;
import com.furioussoulk.agent.stream.worker.register.ServiceNameService;
import com.furioussoulk.agent.stream.worker.trace.TraceSegmentService;
import com.furioussoulk.apm.collector.core.exception.ServiceNotProvidedException;
import com.furioussoulk.apm.collector.core.module.Module;
import com.furioussoulk.apm.collector.core.module.ModuleProvider;

import java.util.Properties;

public class AgentStreamModuleProvider extends ModuleProvider {

    @Override
    public String name() {
        return "default";
    }

    @Override
    public Class<? extends Module> module() {
        return AgentStreamModule.class;
    }

    @Override
    public void prepare(Properties config) throws ServiceNotProvidedException {
        this.registerServiceImplementation(IApplicationIDService.class, new ApplicationIDService(getManager()));
        this.registerServiceImplementation(IInstanceIDService.class, new InstanceIDService(getManager()));
        this.registerServiceImplementation(IServiceNameService.class, new ServiceNameService(getManager()));

        this.registerServiceImplementation(ICpuMetricService.class, new CpuMetricService());
        this.registerServiceImplementation(IGCMetricService.class, new GCMetricService());
        this.registerServiceImplementation(IMemoryMetricService.class, new MemoryMetricService());
        this.registerServiceImplementation(IMemoryPoolMetricService.class, new MemoryPoolMetricService());
        this.registerServiceImplementation(IInstanceHeartBeatService.class, new InstanceHeartBeatService());

        this.registerServiceImplementation(ITraceSegmentService.class, new TraceSegmentService(getManager()));

        BufferFileConfig.Parser parser = new BufferFileConfig.Parser();
        parser.parse(config);
    }

    @Override
    public void start(Properties config) throws ServiceNotProvidedException {
        RemoteDataRegisterService remoteDataRegisterService = getManager().find(RemoteModule.NAME).getService(RemoteDataRegisterService.class);
        AgentStreamRemoteDataRegister agentStreamRemoteDataRegister = new AgentStreamRemoteDataRegister(remoteDataRegisterService);
        agentStreamRemoteDataRegister.register();

        AgentStreamBootStartup bootStartup = new AgentStreamBootStartup(getManager());
        bootStartup.start();
    }

    @Override
    public void notifyAfterCompleted() throws ServiceNotProvidedException {

    }

    @Override
    public String[] requiredModules() {
        return new String[]{StorageModule.NAME, CacheModule.NAME};
    }
}
