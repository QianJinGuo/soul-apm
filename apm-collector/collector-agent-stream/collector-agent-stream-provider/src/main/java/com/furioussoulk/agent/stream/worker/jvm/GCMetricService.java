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

package com.furioussoulk.agent.stream.worker.jvm;

import org.skywalking.apm.collector.agent.stream.graph.JvmMetricStreamGraph;
import org.skywalking.apm.collector.agent.stream.service.jvm.IGCMetricService;
import org.skywalking.apm.collector.core.graph.Graph;
import org.skywalking.apm.collector.core.graph.GraphManager;
import org.skywalking.apm.collector.core.util.Const;
import org.skywalking.apm.collector.core.util.ObjectUtils;
import org.skywalking.apm.collector.storage.table.jvm.GCMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author peng-yongsheng
 */
public class GCMetricService implements IGCMetricService {

    private final Logger logger = LoggerFactory.getLogger(GCMetricService.class);

    private Graph<GCMetric> gcMetricGraph;

    private Graph<GCMetric> getGcMetricGraph() {
        if (ObjectUtils.isEmpty(gcMetricGraph)) {
            gcMetricGraph = GraphManager.INSTANCE.createIfAbsent(JvmMetricStreamGraph.GC_METRIC_GRAPH_ID, GCMetric.class);
        }
        return gcMetricGraph;
    }

    @Override public void send(int instanceId, long timeBucket, int phraseValue, long count, long time) {
        GCMetric gcMetric = new GCMetric(timeBucket + Const.ID_SPLIT + instanceId + Const.ID_SPLIT + String.valueOf(phraseValue));
        gcMetric.setInstanceId(instanceId);
        gcMetric.setPhrase(phraseValue);
        gcMetric.setCount(count);
        gcMetric.setTime(time);
        gcMetric.setTimeBucket(timeBucket);

        logger.debug("push to gc metric graph, id: {}", gcMetric.getId());
        getGcMetricGraph().start(gcMetric);
    }
}
