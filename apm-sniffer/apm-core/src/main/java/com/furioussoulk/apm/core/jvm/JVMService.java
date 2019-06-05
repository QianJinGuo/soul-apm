package com.furioussoulk.apm.core.jvm;

import com.furioussoulk.apm.core.boot.BootService;
import com.furioussoulk.apm.core.boot.DefaultNamedThreadFactory;
import com.furioussoulk.apm.core.boot.ServiceManager;
import com.furioussoulk.apm.core.conf.Config;
import com.furioussoulk.apm.core.logger.LogManager;
import com.furioussoulk.apm.core.logger.api.ILogger;
import com.furioussoulk.apm.core.remote.GRPCChannelManager;
import com.furioussoulk.network.proto.JVMMetric;
import io.grpc.ManagedChannel;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


/**
 * The <code>JVMService</code> represents a timer,
 * which collectors JVM cpu, memory, memorypool and gc info,
 * and send the collected info to Collector through the channel provided by {@link GRPCChannelManager}
 *
 */
public class JVMService implements BootService, Runnable {
    private static final ILogger logger = LogManager.getLogger(JVMService.class);
    private LinkedBlockingQueue<JVMMetric> queue;
    private volatile ScheduledFuture<?> collectMetricFuture;
    private volatile ScheduledFuture<?> sendMetricFuture;
    private Sender sender;
    @Override
    public void beforeBoot() throws Throwable {
        queue = new LinkedBlockingQueue(Config.Jvm.BUFFER_SIZE);
        sender = new Sender();
        ServiceManager.INSTANCE.findService(GRPCChannelManager.class).addChannelListener(sender);
    }

    @Override
    public void boot() throws Throwable {
        collectMetricFuture = Executors
            .newSingleThreadScheduledExecutor(new DefaultNamedThreadFactory("JVMService-produce"))
            .scheduleAtFixedRate(this, 0, 1, TimeUnit.SECONDS);
        sendMetricFuture = Executors
            .newSingleThreadScheduledExecutor(new DefaultNamedThreadFactory("JVMService-consume"))
            .scheduleAtFixedRate(sender, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void afterBoot() throws Throwable {

    }

    @Override
    public void shutdown() throws Throwable {
        collectMetricFuture.cancel(true);
        sendMetricFuture.cancel(true);
    }

    @Override
    public void run() {
        if (RemoteDownstreamConfig.Agent.APPLICATION_ID != DictionaryUtil.nullValue()
            && RemoteDownstreamConfig.Agent.APPLICATION_INSTANCE_ID != DictionaryUtil.nullValue()
            ) {
            long currentTimeMillis = System.currentTimeMillis();
            try {
                JVMMetric.Builder jvmBuilder = JVMMetric.newBuilder();
                jvmBuilder.setTime(currentTimeMillis);
                jvmBuilder.setCpu(CPUProvider.INSTANCE.getCpuMetric());
                jvmBuilder.addAllMemory(MemoryProvider.INSTANCE.getMemoryMetricList());
                jvmBuilder.addAllMemoryPool(MemoryPoolProvider.INSTANCE.getMemoryPoolMetricList());
                jvmBuilder.addAllGc(GCProvider.INSTANCE.getGCList());

                JVMMetric jvmMetric = jvmBuilder.build();
                if (!queue.offer(jvmMetric)) {
                    queue.poll();
                    queue.offer(jvmMetric);
                }
            } catch (Exception e) {
                logger.error(e, "Collect JVM info fail.");
            }
        }
    }

    private class Sender implements Runnable, GRPCChannelListener {
        private volatile GRPCChannelStatus status = GRPCChannelStatus.DISCONNECT;
        private volatile JVMMetricsServiceGrpc.JVMMetricsServiceBlockingStub stub = null;

        @Override
        public void run() {
            if (RemoteDownstreamConfig.Agent.APPLICATION_ID != DictionaryUtil.nullValue()
                && RemoteDownstreamConfig.Agent.APPLICATION_INSTANCE_ID != DictionaryUtil.nullValue()
                ) {
                if (status == GRPCChannelStatus.CONNECTED) {
                    try {
                        JVMMetrics.Builder builder = JVMMetrics.newBuilder();
                        LinkedList<JVMMetric> buffer = new LinkedList<JVMMetric>();
                        queue.drainTo(buffer);
                        if (buffer.size() > 0) {
                            builder.addAllMetrics(buffer);
                            builder.setApplicationInstanceId(RemoteDownstreamConfig.Agent.APPLICATION_INSTANCE_ID);
                            stub.collect(builder.build());
                        }
                    } catch (Throwable t) {
                        logger.error(t, "send JVM metrics to Collector fail.");
                    }
                }
            }
        }

        @Override
        public void statusChanged(GRPCChannelStatus status) {
            if (CONNECTED.equals(status)) {
                ManagedChannel channel = ServiceManager.INSTANCE.findService(GRPCChannelManager.class).getManagedChannel();
                stub = JVMMetricsServiceGrpc.newBlockingStub(channel);
            }
            this.status = status;
        }
    }
}
