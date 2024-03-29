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

package com.furioussoulk.agent.stream.worker.trace.service;

import org.skywalking.apm.collector.agent.stream.graph.TraceStreamGraph;
import org.skywalking.apm.collector.agent.stream.parser.EntrySpanListener;
import org.skywalking.apm.collector.agent.stream.parser.FirstSpanListener;
import org.skywalking.apm.collector.agent.stream.parser.RefsListener;
import org.skywalking.apm.collector.agent.stream.parser.standardization.ReferenceDecorator;
import org.skywalking.apm.collector.agent.stream.parser.standardization.SpanDecorator;
import org.skywalking.apm.collector.cache.CacheModule;
import org.skywalking.apm.collector.cache.service.ServiceNameCacheService;
import org.skywalking.apm.collector.core.graph.Graph;
import org.skywalking.apm.collector.core.graph.GraphManager;
import org.skywalking.apm.collector.core.module.ModuleManager;
import org.skywalking.apm.collector.core.util.Const;
import org.skywalking.apm.collector.core.util.TimeBucketUtils;
import org.skywalking.apm.collector.storage.table.service.ServiceEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author peng-yongsheng
 */
public class ServiceEntrySpanListener implements RefsListener, FirstSpanListener, EntrySpanListener {

    private final Logger logger = LoggerFactory.getLogger(ServiceEntrySpanListener.class);

    private long timeBucket;
    private boolean hasReference = false;
    private int applicationId;
    private int entryServiceId;
    private String entryServiceName;
    private boolean hasEntry = false;
    private final ServiceNameCacheService serviceNameCacheService;

    public ServiceEntrySpanListener(ModuleManager moduleManager) {
        this.serviceNameCacheService = moduleManager.find(CacheModule.NAME).getService(ServiceNameCacheService.class);
    }

    @Override
    public void parseEntry(SpanDecorator spanDecorator, int applicationId, int instanceId,
        String segmentId) {
        this.applicationId = applicationId;
        this.entryServiceId = spanDecorator.getOperationNameId();
        this.entryServiceName = serviceNameCacheService.getSplitServiceName(serviceNameCacheService.get(entryServiceId));
        this.hasEntry = true;
    }

    @Override public void parseRef(ReferenceDecorator referenceDecorator, int applicationId, int instanceId,
        String segmentId) {
        hasReference = true;
    }

    @Override
    public void parseFirst(SpanDecorator spanDecorator, int applicationId, int instanceId,
        String segmentId) {
        timeBucket = TimeBucketUtils.INSTANCE.getMinuteTimeBucket(spanDecorator.getStartTime());
    }

    @Override public void build() {
        logger.debug("entry service listener build");
        if (!hasReference && hasEntry) {
            ServiceEntry serviceEntry = new ServiceEntry(applicationId + Const.ID_SPLIT + entryServiceId);
            serviceEntry.setApplicationId(applicationId);
            serviceEntry.setEntryServiceId(entryServiceId);
            serviceEntry.setEntryServiceName(entryServiceName);
            serviceEntry.setRegisterTime(timeBucket);
            serviceEntry.setNewestTime(timeBucket);

            logger.debug("push to service entry aggregation worker, id: {}", serviceEntry.getId());
            Graph<ServiceEntry> graph = GraphManager.INSTANCE.createIfAbsent(TraceStreamGraph.SERVICE_ENTRY_GRAPH_ID, ServiceEntry.class);
            graph.start(serviceEntry);
        }
    }
}
