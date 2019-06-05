package com.furioussoulk.collector.agent.grpc.handler;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.grpc.stub.StreamObserver;
import org.skywalking.apm.collector.agent.stream.AgentStreamModule;
import org.skywalking.apm.collector.agent.stream.service.register.IInstanceIDService;
import org.skywalking.apm.collector.core.module.ModuleManager;
import org.skywalking.apm.collector.core.util.TimeBucketUtils;
import org.skywalking.apm.collector.server.grpc.GRPCHandler;
import org.skywalking.apm.network.proto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstanceDiscoveryServiceHandler extends InstanceDiscoveryServiceGrpc.InstanceDiscoveryServiceImplBase implements GRPCHandler {

    private final Logger logger = LoggerFactory.getLogger(InstanceDiscoveryServiceHandler.class);

    private final IInstanceIDService instanceIDService;

    public InstanceDiscoveryServiceHandler(ModuleManager moduleManager) {
        this.instanceIDService = moduleManager.find(AgentStreamModule.NAME).getService(IInstanceIDService.class);
    }

    @Override
    public void register(ApplicationInstance request, StreamObserver<ApplicationInstanceMapping> responseObserver) {
        long timeBucket = TimeBucketUtils.INSTANCE.getSecondTimeBucket(request.getRegisterTime());
        int instanceId = instanceIDService.getOrCreate(request.getApplicationId(), request.getAgentUUID(), timeBucket, buildOsInfo(request.getOsinfo()));
        ApplicationInstanceMapping.Builder builder = ApplicationInstanceMapping.newBuilder();
        builder.setApplicationId(request.getApplicationId());
        builder.setApplicationInstanceId(instanceId);
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void registerRecover(ApplicationInstanceRecover request, StreamObserver<Downstream> responseObserver) {
        long timeBucket = TimeBucketUtils.INSTANCE.getSecondTimeBucket(request.getRegisterTime());
        instanceIDService.recover(request.getApplicationInstanceId(), request.getApplicationId(), timeBucket, buildOsInfo(request.getOsinfo()));
        responseObserver.onNext(Downstream.newBuilder().build());
        responseObserver.onCompleted();
    }

    private String buildOsInfo(OSInfo osinfo) {
        JsonObject osInfoJson = new JsonObject();
        osInfoJson.addProperty("osName", osinfo.getOsName());
        osInfoJson.addProperty("hostName", osinfo.getHostname());
        osInfoJson.addProperty("processId", osinfo.getProcessNo());

        JsonArray ipv4Array = new JsonArray();
        for (String ipv4 : osinfo.getIpv4SList()) {
            ipv4Array.add(ipv4);
        }
        osInfoJson.add("ipv4s", ipv4Array);
        return osInfoJson.toString();
    }
}
