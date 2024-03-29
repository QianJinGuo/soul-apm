/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.skywalking.oap.server.cluster.plugin.kubernetes;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.*;
import org.apache.skywalking.oap.server.core.cluster.ClusterNodesQuery;
import org.apache.skywalking.oap.server.core.cluster.ClusterRegister;
import org.apache.skywalking.oap.server.core.cluster.RemoteInstance;
import org.apache.skywalking.oap.server.core.cluster.ServiceRegisterException;
import org.apache.skywalking.oap.server.core.remote.client.Address;
import org.apache.skywalking.oap.server.telemetry.api.TelemetryRelatedContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * Read collector pod info from api-server of kubernetes, then using all containerIp list to
 * construct the list of {@link RemoteInstance}.
 *
 * @author gaohongtao
 */
public class KubernetesCoordinator implements ClusterRegister, ClusterNodesQuery {

    private static final Logger logger = LoggerFactory.getLogger(KubernetesCoordinator.class);

    private final String uid;

    private final Map<String, RemoteInstance> cache = new ConcurrentHashMap<>();

    private final ReusableWatch<Event> watch;

    private int port;

    KubernetesCoordinator(final ReusableWatch<Event> watch, final Supplier<String> uidSupplier) {
        this.watch = watch;
        this.uid = uidSupplier.get();
        TelemetryRelatedContext.INSTANCE.setId(uid);
    }

    @Override
    public void registerRemote(RemoteInstance remoteInstance) throws ServiceRegisterException {
        this.port = remoteInstance.getAddress().getPort();
        submitTask(MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor(new ThreadFactoryBuilder()
                .setDaemon(true).setNameFormat("Kubernetes-ApiServer-%s").build())));
    }

    private void submitTask(final ListeningExecutorService service) {
        watch.initOrReset();
        ListenableFuture<?> watchFuture = service.submit(newWatch());
        Futures.addCallback(watchFuture, new FutureCallback<Object>() {
            @Override
            public void onSuccess(@Nullable Object ignored) {
                submitTask(service);
            }

            @Override
            public void onFailure(@Nullable Throwable throwable) {
                logger.debug("Generate remote nodes error", throwable);
                submitTask(service);
            }
        });
    }

    private Callable<Object> newWatch() {
        return () -> {
            generateRemoteNodes();
            return null;
        };
    }

    private void generateRemoteNodes() {
        for (Event event : watch) {
            if (event == null) {
                break;
            }
            logger.debug("Received event {} {}-{}", event.getType(), event.getUid(), event.getHost());
            switch (event.getType()) {
                case "ADDED":
                case "MODIFIED":
                    cache.put(event.getUid(), new RemoteInstance(new Address(event.getHost(), port, event.getUid().equals(this.uid))));
                    break;
                case "DELETED":
                    cache.remove(event.getUid());
                    break;
                default:
                    throw new RuntimeException(String.format("Unknown event %s", event.getType()));
            }
        }
    }

    @Override
    public List<RemoteInstance> queryRemoteNodes() {
        logger.debug("Query kubernetes remote nodes: {}", cache);
        return Lists.newArrayList(cache.values());
    }
}
