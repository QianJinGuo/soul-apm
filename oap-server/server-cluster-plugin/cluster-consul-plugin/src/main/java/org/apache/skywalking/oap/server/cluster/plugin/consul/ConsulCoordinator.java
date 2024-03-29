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

package org.apache.skywalking.oap.server.cluster.plugin.consul;

import com.google.common.base.Strings;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;
import com.orbitz.consul.model.health.ServiceHealth;
import org.apache.skywalking.oap.server.core.cluster.ClusterNodesQuery;
import org.apache.skywalking.oap.server.core.cluster.ClusterRegister;
import org.apache.skywalking.oap.server.core.cluster.RemoteInstance;
import org.apache.skywalking.oap.server.core.cluster.ServiceRegisterException;
import org.apache.skywalking.oap.server.core.remote.client.Address;
import org.apache.skywalking.oap.server.library.util.CollectionUtils;
import org.apache.skywalking.oap.server.telemetry.api.TelemetryRelatedContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author peng-yongsheng
 */
public class ConsulCoordinator implements ClusterRegister, ClusterNodesQuery {

    private final Consul client;
    private final String serviceName;
    private volatile Address selfAddress;

    public ConsulCoordinator(Consul client, String serviceName) {
        this.client = client;
        this.serviceName = serviceName;
    }

    @Override
    public List<RemoteInstance> queryRemoteNodes() {
        HealthClient healthClient = client.healthClient();

        // Discover only "passing" nodes
        List<ServiceHealth> nodes = healthClient.getHealthyServiceInstances(serviceName).getResponse();

        List<RemoteInstance> remoteInstances = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(nodes)) {
            nodes.forEach(node -> {
                if (!Strings.isNullOrEmpty(node.getService().getAddress())) {
                    if (Objects.nonNull(selfAddress)) {
                        if (selfAddress.getHost().equals(node.getService().getAddress()) && selfAddress.getPort() == node.getService().getPort()) {
                            remoteInstances.add(new RemoteInstance(new Address(node.getService().getAddress(), node.getService().getPort(), true)));
                        } else {
                            remoteInstances.add(new RemoteInstance(new Address(node.getService().getAddress(), node.getService().getPort(), false)));
                        }
                    }
                }
            });
        }
        return remoteInstances;
    }

    @Override
    public void registerRemote(RemoteInstance remoteInstance) throws ServiceRegisterException {
        AgentClient agentClient = client.agentClient();

        this.selfAddress = remoteInstance.getAddress();
        TelemetryRelatedContext.INSTANCE.setId(selfAddress.toString());

        Registration registration = ImmutableRegistration.builder()
                .id(remoteInstance.getAddress().toString())
                .name(serviceName)
                .address(remoteInstance.getAddress().getHost())
                .port(remoteInstance.getAddress().getPort())
                .check(Registration.RegCheck.grpc(remoteInstance.getAddress().getHost() + ":" + remoteInstance.getAddress().getPort(), 5)) // registers with a TTL of 5 seconds
                .build();

        agentClient.register(registration);
    }
}
