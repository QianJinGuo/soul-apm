package com.furioussoulk.apm.core.remote;

import com.furioussoulk.apm.core.boot.BootService;
import com.furioussoulk.apm.core.boot.DefaultNamedThreadFactory;
import com.furioussoulk.apm.core.conf.Config;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * The <code>CollectorDiscoveryService</code> is responsible for start {@link DiscoveryRestServiceClient}.
 *
 */
public class CollectorDiscoveryService implements BootService {
    private ScheduledFuture<?> future;

    @Override
    public void beforeBoot() throws Throwable {

    }

    @Override
    public void boot() throws Throwable {
        future = Executors.newSingleThreadScheduledExecutor(new DefaultNamedThreadFactory("CollectorDiscoveryService"))
            .scheduleAtFixedRate(new DiscoveryRestServiceClient(), 0,
                Config.Collector.DISCOVERY_CHECK_INTERVAL, TimeUnit.SECONDS);
    }

    @Override
    public void afterBoot() throws Throwable {

    }

    @Override
    public void shutdown() throws Throwable {
        future.cancel(true);
    }
}
