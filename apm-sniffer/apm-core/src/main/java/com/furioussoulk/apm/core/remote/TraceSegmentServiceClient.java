package com.furioussoulk.apm.core.remote;

import com.furioussoulk.apm.common.datacarrier.DataCarrier;
import com.furioussoulk.apm.common.datacarrier.buffer.BufferStrategy;
import com.furioussoulk.apm.common.datacarrier.consumer.IConsumer;
import com.furioussoulk.apm.core.boot.BootService;
import com.furioussoulk.apm.core.boot.ServiceManager;
import com.furioussoulk.apm.core.context.TracingContext;
import com.furioussoulk.apm.core.context.TracingContextListener;
import com.furioussoulk.apm.core.context.trace.TraceSegment;
import com.furioussoulk.apm.core.logger.LogManager;
import com.furioussoulk.apm.core.logger.api.ILogger;
import com.furioussoulk.network.proto.Downstream;
import com.furioussoulk.network.proto.TraceSegmentServiceGrpc;
import com.furioussoulk.network.proto.UpstreamSegment;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import java.util.List;

import static com.furioussoulk.apm.core.conf.Config.Buffer.BUFFER_SIZE;
import static com.furioussoulk.apm.core.conf.Config.Buffer.CHANNEL_SIZE;
import static com.furioussoulk.apm.core.remote.GRPCChannelStatus.CONNECTED;

public class TraceSegmentServiceClient implements BootService, IConsumer<TraceSegment>, TracingContextListener, GRPCChannelListener {
    private static final ILogger logger = LogManager.getLogger(TraceSegmentServiceClient.class);
    private static final int TIMEOUT = 30 * 1000;

    private long lastLogTime;
    private long segmentUplinkedCounter;
    private long segmentAbandonedCounter;
    private volatile DataCarrier<TraceSegment> carrier;
    private volatile TraceSegmentServiceGrpc.TraceSegmentServiceStub serviceStub;
    private volatile GRPCChannelStatus status = GRPCChannelStatus.DISCONNECT;

    @Override
    public void beforeBoot() throws Throwable {
        ServiceManager.INSTANCE.findService(GRPCChannelManager.class).addChannelListener(this);
    }

    @Override
    public void boot() throws Throwable {
        lastLogTime = System.currentTimeMillis();
        segmentUplinkedCounter = 0;
        segmentAbandonedCounter = 0;
        carrier = new DataCarrier<TraceSegment>(CHANNEL_SIZE, BUFFER_SIZE);
        carrier.setBufferStrategy(BufferStrategy.IF_POSSIBLE);
        carrier.consume(this, 1);
    }

    @Override
    public void afterBoot() throws Throwable {
        TracingContext.ListenerManager.add(this);
    }

    @Override
    public void shutdown() throws Throwable {
        carrier.shutdownConsumers();
    }

    @Override
    public void init() {

    }

    @Override
    public void consume(List<TraceSegment> data) {
        if (CONNECTED.equals(status)) {
            final GRPCStreamServiceStatus status = new GRPCStreamServiceStatus(false);
            StreamObserver<UpstreamSegment> upstreamSegmentStreamObserver = serviceStub.collect(new StreamObserver<Downstream>() {
                @Override
                public void onNext(Downstream downstream) {

                }

                @Override
                public void onError(Throwable throwable) {
                    status.finished();
                    if (logger.isErrorEnable()) {
                        logger.error(throwable, "Send UpstreamSegment to collector fail with a grpc internal exception.");
                    }
                    ServiceManager.INSTANCE.findService(GRPCChannelManager.class).reportError(throwable);
                }

                @Override
                public void onCompleted() {
                    status.finished();
                }
            });

            for (TraceSegment segment : data) {
                try {
                    UpstreamSegment upstreamSegment = segment.transform();
                    upstreamSegmentStreamObserver.onNext(upstreamSegment);
                } catch (Throwable t) {
                    logger.error(t, "Transform and send UpstreamSegment to collector fail.");
                }
            }
            upstreamSegmentStreamObserver.onCompleted();

            if (status.wait4Finish(TIMEOUT)) {
                segmentUplinkedCounter += data.size();
            }
        } else {
            segmentAbandonedCounter += data.size();
        }

        printUplinkStatus();
    }

    private void printUplinkStatus() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastLogTime > 30 * 1000) {
            lastLogTime = currentTimeMillis;
            if (segmentUplinkedCounter > 0) {
                logger.debug("{} trace segments have been sent to collector.", segmentUplinkedCounter);
                segmentUplinkedCounter = 0;
            }
            if (segmentAbandonedCounter > 0) {
                logger.debug("{} trace segments have been abandoned, cause by no available channel.", segmentAbandonedCounter);
                segmentAbandonedCounter = 0;
            }
        }
    }

    @Override
    public void onError(List<TraceSegment> data, Throwable t) {
        logger.error(t, "Try to send {} trace segments to collector, with unexpected exception.", data.size());
    }

    @Override
    public void onExit() {

    }

    @Override
    public void afterFinished(TraceSegment traceSegment) {
        if (traceSegment.isIgnore()) {
            return;
        }
        if (!carrier.produce(traceSegment)) {
            if (logger.isDebugEnable()) {
                logger.debug("One trace segment has been abandoned, cause by buffer is full.");
            }
        }
    }

    @Override
    public void statusChanged(GRPCChannelStatus status) {
        if (CONNECTED.equals(status)) {
            ManagedChannel channel = ServiceManager.INSTANCE.findService(GRPCChannelManager.class).getManagedChannel();
            serviceStub = TraceSegmentServiceGrpc.newStub(channel);
        }
        this.status = status;
    }
}
