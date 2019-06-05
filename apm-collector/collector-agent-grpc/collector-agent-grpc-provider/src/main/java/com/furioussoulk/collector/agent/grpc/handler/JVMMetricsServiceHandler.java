package com.furioussoulk.collector.agent.grpc.handler;

import io.grpc.stub.StreamObserver;
import org.skywalking.apm.collector.agent.stream.AgentStreamModule;
import org.skywalking.apm.collector.agent.stream.service.jvm.*;
import org.skywalking.apm.collector.core.module.ModuleManager;
import org.skywalking.apm.collector.core.util.TimeBucketUtils;
import org.skywalking.apm.collector.server.grpc.GRPCHandler;
import org.skywalking.apm.network.proto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JVMMetricsServiceHandler extends JVMMetricsServiceGrpc.JVMMetricsServiceImplBase implements GRPCHandler {

    private final Logger logger = LoggerFactory.getLogger(JVMMetricsServiceHandler.class);

    private final ICpuMetricService cpuMetricService;
    private final IGCMetricService gcMetricService;
    private final IMemoryMetricService memoryMetricService;
    private final IMemoryPoolMetricService memoryPoolMetricService;
    private final IInstanceHeartBeatService instanceHeartBeatService;

    public JVMMetricsServiceHandler(ModuleManager moduleManager) {
        this.cpuMetricService = moduleManager.find(AgentStreamModule.NAME).getService(ICpuMetricService.class);
        this.gcMetricService = moduleManager.find(AgentStreamModule.NAME).getService(IGCMetricService.class);
        this.memoryMetricService = moduleManager.find(AgentStreamModule.NAME).getService(IMemoryMetricService.class);
        this.memoryPoolMetricService = moduleManager.find(AgentStreamModule.NAME).getService(IMemoryPoolMetricService.class);
        this.instanceHeartBeatService = moduleManager.find(AgentStreamModule.NAME).getService(IInstanceHeartBeatService.class);
    }

    @Override public void collect(JVMMetrics request, StreamObserver<Downstream> responseObserver) {
        int instanceId = request.getApplicationInstanceId();
        logger.debug("receive the jvm metric from application instance, id: {}", instanceId);

        request.getMetricsList().forEach(metric -> {
            long time = TimeBucketUtils.INSTANCE.getSecondTimeBucket(metric.getTime());
            sendToInstanceHeartBeatService(instanceId, metric.getTime());
            sendToCpuMetricService(instanceId, time, metric.getCpu());
            sendToMemoryMetricService(instanceId, time, metric.getMemoryList());
            sendToMemoryPoolMetricService(instanceId, time, metric.getMemoryPoolList());
            sendToGCMetricService(instanceId, time, metric.getGcList());
        });

        responseObserver.onNext(Downstream.newBuilder().build());
        responseObserver.onCompleted();
    }

    private void sendToInstanceHeartBeatService(int instanceId, long heartBeatTime) {
        instanceHeartBeatService.send(instanceId, heartBeatTime);
    }

    private void sendToMemoryMetricService(int instanceId, long timeBucket, List<Memory> memories) {
        memories.forEach(memory -> memoryMetricService.send(instanceId, timeBucket, memory.getIsHeap(), memory.getInit(), memory.getMax(), memory.getUsed(), memory.getCommitted()));
    }

    private void sendToMemoryPoolMetricService(int instanceId, long timeBucket,
        List<MemoryPool> memoryPools) {

        memoryPools.forEach(memoryPool -> memoryPoolMetricService.send(instanceId, timeBucket, memoryPool.getType().getNumber(), memoryPool.getInit(), memoryPool.getMax(), memoryPool.getUsed(), memoryPool.getCommited()));
    }

    private void sendToCpuMetricService(int instanceId, long timeBucket, CPU cpu) {
        cpuMetricService.send(instanceId, timeBucket, cpu.getUsagePercent());
    }

    private void sendToGCMetricService(int instanceId, long timeBucket, List<GC> gcs) {
        gcs.forEach(gc -> gcMetricService.send(instanceId, timeBucket, gc.getPhraseValue(), gc.getCount(), gc.getTime()));
    }
}
