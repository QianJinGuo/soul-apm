package com.furioussoulk.apm.core.remote;

import com.furioussoulk.apm.core.boot.BootService;
import com.furioussoulk.apm.core.boot.DefaultNamedThreadFactory;
import com.furioussoulk.apm.core.boot.ServiceManager;
import com.furioussoulk.apm.core.conf.Config;
import com.furioussoulk.apm.core.conf.RemoteDownstreamConfig;
import com.furioussoulk.apm.core.context.TracingContext;
import com.furioussoulk.apm.core.context.TracingContextListener;
import com.furioussoulk.apm.core.context.trace.TraceSegment;
import com.furioussoulk.apm.core.dictionary.ApplicationDictionary;
import com.furioussoulk.apm.core.dictionary.DictionaryUtil;
import com.furioussoulk.apm.core.dictionary.OperationNameDictionary;
import com.furioussoulk.apm.core.logger.LogManager;
import com.furioussoulk.apm.core.logger.api.ILogger;
import com.furioussoulk.apm.core.os.OSUtil;
import com.furioussoulk.network.proto.*;
import io.grpc.ManagedChannel;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.furioussoulk.apm.core.remote.GRPCChannelStatus.CONNECTED;

public class AppAndServiceRegisterClient
        implements BootService, GRPCChannelListener,
        Runnable, TracingContextListener {
    private static final ILogger logger = LogManager.getLogger(AppAndServiceRegisterClient.class);
    private static final String PROCESS_UUID = UUID.randomUUID().toString().replaceAll("-", "");

    private volatile GRPCChannelStatus status = GRPCChannelStatus.DISCONNECT;
    private volatile ApplicationRegisterServiceGrpc.ApplicationRegisterServiceBlockingStub applicationRegisterServiceBlockingStub;
    private volatile InstanceDiscoveryServiceGrpc.InstanceDiscoveryServiceBlockingStub instanceDiscoveryServiceBlockingStub;
    private volatile ServiceNameDiscoveryServiceGrpc.ServiceNameDiscoveryServiceBlockingStub serviceNameDiscoveryServiceBlockingStub;
    private volatile ScheduledFuture<?> applicationRegisterFuture;
    private volatile boolean needRegisterRecover = false;
    private volatile long lastSegmentTime = -1;

    @Override
    public void statusChanged(GRPCChannelStatus status) {
        if (CONNECTED.equals(status)) {
            ManagedChannel channel = ServiceManager.INSTANCE.findService(GRPCChannelManager.class).getManagedChannel();
            applicationRegisterServiceBlockingStub = ApplicationRegisterServiceGrpc.newBlockingStub(channel);
            instanceDiscoveryServiceBlockingStub = InstanceDiscoveryServiceGrpc.newBlockingStub(channel);
            if (RemoteDownstreamConfig.Agent.APPLICATION_INSTANCE_ID != DictionaryUtil.nullValue()) {
                needRegisterRecover = true;
            }
            serviceNameDiscoveryServiceBlockingStub = ServiceNameDiscoveryServiceGrpc.newBlockingStub(channel);
        } else {
            applicationRegisterServiceBlockingStub = null;
            instanceDiscoveryServiceBlockingStub = null;
            serviceNameDiscoveryServiceBlockingStub = null;
        }
        this.status = status;
    }

    @Override
    public void beforeBoot() throws Throwable {
        ServiceManager.INSTANCE.findService(GRPCChannelManager.class).addChannelListener(this);
    }

    @Override
    public void boot() throws Throwable {
        applicationRegisterFuture = Executors
                .newSingleThreadScheduledExecutor(new DefaultNamedThreadFactory("AppAndServiceRegisterClient"))
                .scheduleAtFixedRate(this, 0, Config.Collector.APP_AND_SERVICE_REGISTER_CHECK_INTERVAL, TimeUnit.SECONDS);
    }

    @Override
    public void afterBoot() throws Throwable {
        TracingContext.ListenerManager.add(this);
    }

    @Override
    public void shutdown() throws Throwable {
        applicationRegisterFuture.cancel(true);
    }

    @Override
    public void run() {
        logger.debug("AppAndServiceRegisterClient running, status:{}.", status);
        boolean shouldTry = true;
        while (CONNECTED.equals(status) && shouldTry) {
            shouldTry = false;
            try {
                if (RemoteDownstreamConfig.Agent.APPLICATION_ID == DictionaryUtil.nullValue()) {
                    if (applicationRegisterServiceBlockingStub != null) {
                        ApplicationMapping applicationMapping = applicationRegisterServiceBlockingStub.register(
                                Application.newBuilder().addApplicationCode(Config.Agent.APPLICATION_CODE).build());
                        if (applicationMapping.getApplicationCount() > 0) {
                            RemoteDownstreamConfig.Agent.APPLICATION_ID = applicationMapping.getApplication(0).getValue();
                            shouldTry = true;
                        }
                    }
                } else {
                    if (instanceDiscoveryServiceBlockingStub != null) {
                        if (RemoteDownstreamConfig.Agent.APPLICATION_INSTANCE_ID == DictionaryUtil.nullValue()) {

                            ApplicationInstanceMapping instanceMapping = instanceDiscoveryServiceBlockingStub.register(ApplicationInstance.newBuilder()
                                    .setApplicationId(RemoteDownstreamConfig.Agent.APPLICATION_ID)
                                    .setAgentUUID(PROCESS_UUID)
                                    .setRegisterTime(System.currentTimeMillis())
                                    .setOsinfo(OSUtil.buildOSInfo())
                                    .build());
                            if (instanceMapping.getApplicationInstanceId() != DictionaryUtil.nullValue()) {
                                RemoteDownstreamConfig.Agent.APPLICATION_INSTANCE_ID
                                        = instanceMapping.getApplicationInstanceId();
                            }
                        } else {
                            if (needRegisterRecover) {
                                instanceDiscoveryServiceBlockingStub.registerRecover(ApplicationInstanceRecover.newBuilder()
                                        .setApplicationId(RemoteDownstreamConfig.Agent.APPLICATION_ID)
                                        .setApplicationInstanceId(RemoteDownstreamConfig.Agent.APPLICATION_INSTANCE_ID)
                                        .setRegisterTime(System.currentTimeMillis())
                                        .setOsinfo(OSUtil.buildOSInfo())
                                        .build());
                                needRegisterRecover = false;
                            } else {
                                if (lastSegmentTime - System.currentTimeMillis() > 60 * 1000) {
                                    instanceDiscoveryServiceBlockingStub.heartbeat(ApplicationInstanceHeartbeat.newBuilder()
                                            .setApplicationInstanceId(RemoteDownstreamConfig.Agent.APPLICATION_INSTANCE_ID)
                                            .setHeartbeatTime(System.currentTimeMillis())
                                            .build());
                                }
                            }

                            ApplicationDictionary.INSTANCE.syncRemoteDictionary(applicationRegisterServiceBlockingStub);
                            OperationNameDictionary.INSTANCE.syncRemoteDictionary(serviceNameDiscoveryServiceBlockingStub);
                        }
                    }
                }
            } catch (Throwable t) {
                logger.error(t, "AppAndServiceRegisterClient execute fail.");
                ServiceManager.INSTANCE.findService(GRPCChannelManager.class).reportError(t);
            }
        }
    }

    @Override
    public void afterFinished(TraceSegment traceSegment) {
        lastSegmentTime = System.currentTimeMillis();
    }
}
