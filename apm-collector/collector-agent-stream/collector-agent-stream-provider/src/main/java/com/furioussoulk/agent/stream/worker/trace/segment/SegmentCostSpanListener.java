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

package com.furioussoulk.agent.stream.worker.trace.segment;

import org.skywalking.apm.collector.agent.stream.graph.TraceStreamGraph;
import org.skywalking.apm.collector.agent.stream.parser.EntrySpanListener;
import org.skywalking.apm.collector.agent.stream.parser.ExitSpanListener;
import org.skywalking.apm.collector.agent.stream.parser.FirstSpanListener;
import org.skywalking.apm.collector.agent.stream.parser.LocalSpanListener;
import org.skywalking.apm.collector.agent.stream.parser.standardization.SpanDecorator;
import org.skywalking.apm.collector.cache.CacheModule;
import org.skywalking.apm.collector.cache.service.ServiceNameCacheService;
import org.skywalking.apm.collector.core.graph.Graph;
import org.skywalking.apm.collector.core.graph.GraphManager;
import org.skywalking.apm.collector.core.module.ModuleManager;
import org.skywalking.apm.collector.core.util.Const;
import org.skywalking.apm.collector.core.util.TimeBucketUtils;
import org.skywalking.apm.collector.storage.table.segment.SegmentCost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author peng-yongsheng
 */
public class SegmentCostSpanListener implements EntrySpanListener, ExitSpanListener, LocalSpanListener, FirstSpanListener {

    private final Logger logger = LoggerFactory.getLogger(SegmentCostSpanListener.class);

    private final List<SegmentCost> segmentCosts;
    private final ServiceNameCacheService serviceNameCacheService;
    private boolean isError = false;
    private long timeBucket;

    public SegmentCostSpanListener(ModuleManager moduleManager) {
        this.segmentCosts = new ArrayList<>();
        this.serviceNameCacheService = moduleManager.find(CacheModule.NAME).getService(ServiceNameCacheService.class);
    }

    @Override
    public void parseFirst(SpanDecorator spanDecorator, int applicationId, int instanceId,
        String segmentId) {
        timeBucket = TimeBucketUtils.INSTANCE.getMinuteTimeBucket(spanDecorator.getStartTime());

        SegmentCost segmentCost = new SegmentCost(Const.EMPTY_STRING);
        segmentCost.setSegmentId(segmentId);
        segmentCost.setApplicationId(applicationId);
        segmentCost.setCost(spanDecorator.getEndTime() - spanDecorator.getStartTime());
        segmentCost.setStartTime(spanDecorator.getStartTime());
        segmentCost.setEndTime(spanDecorator.getEndTime());
        segmentCost.setId(segmentId);
        if (spanDecorator.getOperationNameId() == 0) {
            segmentCost.setServiceName(spanDecorator.getOperationName());
        } else {
            segmentCost.setServiceName(serviceNameCacheService.getSplitServiceName(serviceNameCacheService.get(spanDecorator.getOperationNameId())));
        }

        segmentCosts.add(segmentCost);
        isError = isError || spanDecorator.getIsError();
    }

    @Override
    public void parseEntry(SpanDecorator spanDecorator, int applicationId, int instanceId,
        String segmentId) {
        isError = isError || spanDecorator.getIsError();
    }

    @Override
    public void parseExit(SpanDecorator spanDecorator, int applicationId, int instanceId, String segmentId) {
        isError = isError || spanDecorator.getIsError();
    }

    @Override
    public void parseLocal(SpanDecorator spanDecorator, int applicationId, int instanceId,
        String segmentId) {
        isError = isError || spanDecorator.getIsError();
    }

    @Override public void build() {
        Graph<SegmentCost> graph = GraphManager.INSTANCE.createIfAbsent(TraceStreamGraph.SEGMENT_COST_GRAPH_ID, SegmentCost.class);
        logger.debug("segment cost listener build");
        for (SegmentCost segmentCost : segmentCosts) {
            segmentCost.setIsError(isError);
            segmentCost.setTimeBucket(timeBucket);
            graph.start(segmentCost);
        }
    }
}
