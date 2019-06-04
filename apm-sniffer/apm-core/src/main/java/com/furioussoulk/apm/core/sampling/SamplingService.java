package com.furioussoulk.apm.core.sampling;

import com.furioussoulk.apm.core.boot.BootService;
import com.furioussoulk.apm.core.boot.DefaultNamedThreadFactory;
import com.furioussoulk.apm.core.conf.Config;
import com.furioussoulk.apm.core.logger.LogManager;
import com.furioussoulk.apm.core.logger.api.ILogger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The <code>SamplingService</code> take charge of how to sample the {@link TraceSegment}. Every {@link TraceSegment}s
 * have been traced, but, considering CPU cost of serialization/deserialization, and network bandwidth, the agent do NOT
 * send all of them to collector, if SAMPLING is on.
 * <p>
 * By default, SAMPLING is on, and {@see {@link Config.Agent#SAMPLE_N_PER_3_SECS }}
 *
 */
public class SamplingService implements BootService {
    private static final ILogger logger = LogManager.getLogger(SamplingService.class);

    private volatile boolean on = false;
    private volatile AtomicInteger samplingFactorHolder;
    private volatile ScheduledFuture<?> scheduledFuture;

    @Override
    public void beforeBoot() throws Throwable {

    }

    @Override
    public void boot() throws Throwable {
        if (scheduledFuture != null) {
            /**
             * If {@link #boot()} invokes twice, mostly in test cases,
             * cancel the old one.
             */
            scheduledFuture.cancel(true);
        }
        if (Config.Agent.SAMPLE_N_PER_3_SECS > 0) {
            on = true;
            this.resetSamplingFactor();
            ScheduledExecutorService service = Executors
                .newSingleThreadScheduledExecutor(new DefaultNamedThreadFactory("SamplingService"));
            scheduledFuture = service.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    resetSamplingFactor();
                }
            }, 0, 3, TimeUnit.SECONDS);
            logger.debug("Agent sampling mechanism started. Sample {} traces in 10 seconds.", Config.Agent.SAMPLE_N_PER_3_SECS);
        }
    }

    @Override
    public void afterBoot() throws Throwable {

    }

    @Override
    public void shutdown() throws Throwable {
        scheduledFuture.cancel(true);
    }

    /**
     * @return true, if sampling mechanism is on, and getDefault the sampling factor successfully.
     */
    public boolean trySampling() {
        if (on) {
            int factor = samplingFactorHolder.get();
            if (factor < Config.Agent.SAMPLE_N_PER_3_SECS) {
                boolean success = samplingFactorHolder.compareAndSet(factor, factor + 1);
                return success;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Increase the sampling factor by force,
     * to avoid sampling too many traces.
     * If many distributed traces require sampled,
     * the trace beginning at local, has less chance to be sampled.
     */
    public void forceSampled() {
        if (on) {
            samplingFactorHolder.incrementAndGet();
        }
    }

    private void resetSamplingFactor() {
        samplingFactorHolder = new AtomicInteger(0);
    }
}