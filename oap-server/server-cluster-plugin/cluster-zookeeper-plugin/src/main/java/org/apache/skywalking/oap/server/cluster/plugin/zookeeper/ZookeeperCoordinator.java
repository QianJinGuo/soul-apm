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

package org.apache.skywalking.oap.server.cluster.plugin.zookeeper;

import org.apache.curator.x.discovery.ServiceCache;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.skywalking.oap.server.core.cluster.ClusterNodesQuery;
import org.apache.skywalking.oap.server.core.cluster.ClusterRegister;
import org.apache.skywalking.oap.server.core.cluster.RemoteInstance;
import org.apache.skywalking.oap.server.core.cluster.ServiceRegisterException;
import org.apache.skywalking.oap.server.core.remote.client.Address;
import org.apache.skywalking.oap.server.telemetry.api.TelemetryRelatedContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author peng-yongsheng
 */
public class ZookeeperCoordinator implements ClusterRegister, ClusterNodesQuery {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperCoordinator.class);

    private final ServiceDiscovery<RemoteInstance> serviceDiscovery;
    private volatile ServiceCache<RemoteInstance> serviceCache;
    private volatile Address selfAddress;

    ZookeeperCoordinator(ServiceDiscovery<RemoteInstance> serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    @Override
    public synchronized void registerRemote(RemoteInstance remoteInstance) throws ServiceRegisterException {
        try {
            String remoteNamePath = "remote";

            ServiceInstance<RemoteInstance> thisInstance = ServiceInstance.<RemoteInstance>builder()
                    .name(remoteNamePath)
                    .id(UUID.randomUUID().toString())
                    .address(remoteInstance.getAddress().getHost())
                    .port(remoteInstance.getAddress().getPort())
                    .payload(remoteInstance)
                    .build();

            serviceDiscovery.registerService(thisInstance);

            serviceCache = serviceDiscovery.serviceCacheBuilder()
                    .name(remoteNamePath)
                    .build();

            serviceCache.start();

            this.selfAddress = remoteInstance.getAddress();
            TelemetryRelatedContext.INSTANCE.setId(selfAddress.toString());
        } catch (Exception e) {
            throw new ServiceRegisterException(e.getMessage());
        }
    }

    @Override
    public List<RemoteInstance> queryRemoteNodes() {
        List<RemoteInstance> remoteInstanceDetails = new ArrayList<>(20);
        if (Objects.nonNull(serviceCache)) {
            List<ServiceInstance<RemoteInstance>> serviceInstances = serviceCache.getInstances();

            serviceInstances.forEach(serviceInstance -> {
                RemoteInstance instance = serviceInstance.getPayload();
                if (instance.getAddress().equals(selfAddress)) {
                    instance.getAddress().setSelf(true);
                } else {
                    instance.getAddress().setSelf(false);
                }
                remoteInstanceDetails.add(instance);
            });
        }
        return remoteInstanceDetails;
    }
}
