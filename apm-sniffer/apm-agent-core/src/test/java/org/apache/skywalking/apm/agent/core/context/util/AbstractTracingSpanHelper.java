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


package org.apache.skywalking.apm.agent.core.context.util;

import org.apache.skywalking.apm.agent.core.context.trace.AbstractTracingSpan;
import org.apache.skywalking.apm.agent.core.context.trace.LogDataEntity;

import java.util.Collections;
import java.util.List;

public class AbstractTracingSpanHelper {
    public static int getParentSpanId(AbstractTracingSpan tracingSpan) {
        try {
            return FieldGetter.get2LevelParentFieldValue(tracingSpan, "parentSpanId");
        } catch (Exception e) {
        }

        return -9999;
    }

    public static List<LogDataEntity> getLogs(AbstractTracingSpan tracingSpan) {
        try {
            return FieldGetter.get2LevelParentFieldValue(tracingSpan, "logs");
        } catch (Exception e) {
        }

        return Collections.emptyList();
    }
}
