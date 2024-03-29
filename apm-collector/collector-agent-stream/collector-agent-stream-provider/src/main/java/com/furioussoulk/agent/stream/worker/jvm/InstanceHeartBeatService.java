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
import org.skywalking.apm.collector.agent.stream.service.jvm.IInstanceHeartBeatService;
import org.skywalking.apm.collector.core.graph.Graph;
import org.skywalking.apm.collector.core.graph.GraphManager;
import org.skywalking.apm.collector.core.util.ObjectUtils;
import org.skywalking.apm.collector.core.util.TimeBucketUtils;
import org.skywalking.apm.collector.storage.table.register.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author peng-yongsheng
 */
public class InstanceHeartBeatService implements IInstanceHeartBeatService {

    private final Logger logger = LoggerFactory.getLogger(InstanceHeartBeatService.class);

    private Graph<Instance> heartBeatGraph;

    private Graph<Instance> getHeartBeatGraph() {
        if (ObjectUtils.isEmpty(heartBeatGraph)) {
            this.heartBeatGraph = GraphManager.INSTANCE.createIfAbsent(JvmMetricStreamGraph.INST_HEART_BEAT_GRAPH_ID, Instance.class);
        }
        return heartBeatGraph;
    }

    @Override public void send(int instanceId, long heartBeatTime) {
        Instance instance = new Instance(String.valueOf(instanceId));
        instance.setHeartBeatTime(TimeBucketUtils.INSTANCE.getSecondTimeBucket(heartBeatTime));
        instance.setInstanceId(instanceId);

        logger.debug("push to instance heart beat persistence worker, id: {}", instance.getId());
        getHeartBeatGraph().start(instance);
    }
}
