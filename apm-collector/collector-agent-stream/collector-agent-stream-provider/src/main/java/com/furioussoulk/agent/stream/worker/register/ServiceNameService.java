/*
 * Copyright 2017, OpenSkywalking Organization All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Project repository: https://github.com/OpenSkywalking/skywalking
 */

package com.furioussoulk.agent.stream.worker.register;

import org.skywalking.apm.collector.agent.stream.graph.RegisterStreamGraph;
import org.skywalking.apm.collector.agent.stream.service.register.IServiceNameService;
import org.skywalking.apm.collector.cache.CacheModule;
import org.skywalking.apm.collector.cache.service.ServiceIdCacheService;
import org.skywalking.apm.collector.core.graph.Graph;
import org.skywalking.apm.collector.core.graph.GraphManager;
import org.skywalking.apm.collector.core.module.ModuleManager;
import org.skywalking.apm.collector.core.util.ObjectUtils;
import org.skywalking.apm.collector.storage.table.register.ServiceName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author peng-yongsheng
 */
public class ServiceNameService implements IServiceNameService {

    private final Logger logger = LoggerFactory.getLogger(ServiceNameService.class);

    private final ModuleManager moduleManager;
    private ServiceIdCacheService serviceIdCacheService;
    private Graph<ServiceName> serviceNameRegisterGraph;

    public ServiceNameService(ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
    }

    private ServiceIdCacheService getServiceIdCacheService() {
        if (ObjectUtils.isEmpty(serviceIdCacheService)) {
            serviceIdCacheService = moduleManager.find(CacheModule.NAME).getService(ServiceIdCacheService.class);
        }
        return serviceIdCacheService;
    }

    private Graph<ServiceName> getServiceNameRegisterGraph() {
        if (ObjectUtils.isEmpty(serviceNameRegisterGraph)) {
            this.serviceNameRegisterGraph = GraphManager.INSTANCE.createIfAbsent(RegisterStreamGraph.SERVICE_NAME_REGISTER_GRAPH_ID, ServiceName.class);
        }
        return serviceNameRegisterGraph;
    }

    public int getOrCreate(int applicationId, String serviceName) {
        int serviceId = getServiceIdCacheService().get(applicationId, serviceName);

        if (serviceId == 0) {
            ServiceName service = new ServiceName("0");
            service.setApplicationId(applicationId);
            service.setServiceName(serviceName);
            service.setServiceId(0);

            getServiceNameRegisterGraph().start(service);
        }
        return serviceId;
    }
}
