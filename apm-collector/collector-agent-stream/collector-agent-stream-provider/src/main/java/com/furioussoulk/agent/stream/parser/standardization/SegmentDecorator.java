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

package com.furioussoulk.agent.stream.parser.standardization;

import org.skywalking.apm.network.proto.TraceSegmentObject;
import org.skywalking.apm.network.proto.UniqueId;

/**
 * @author peng-yongsheng
 */
public class SegmentDecorator implements StandardBuilder {
    private boolean isOrigin = true;
    private final TraceSegmentObject segmentObject;
    private TraceSegmentObject.Builder segmentBuilder;

    public SegmentDecorator(TraceSegmentObject segmentObject) {
        this.segmentObject = segmentObject;
    }

    public int getApplicationId() {
        return segmentObject.getApplicationId();
    }

    public int getApplicationInstanceId() {
        return segmentObject.getApplicationInstanceId();
    }

    public UniqueId getTraceSegmentId() {
        return segmentObject.getTraceSegmentId();
    }

    public int getSpansCount() {
        return segmentObject.getSpansCount();
    }

    public SpanDecorator getSpans(int index) {
        if (isOrigin) {
            return new SpanDecorator(segmentObject.getSpans(index), this);
        } else {
            return new SpanDecorator(segmentBuilder.getSpansBuilder(index), this);
        }
    }

    public int getRefsCount() {
        return segmentObject.getRefsCount();
    }

    public ReferenceDecorator getRefs(int index) {
        if (isOrigin) {
            return new ReferenceDecorator(segmentObject.getRefs(index), this);
        } else {
            return new ReferenceDecorator(segmentBuilder.getRefsBuilder(index), this);
        }
    }

    public byte[] toByteArray() {
        if (isOrigin) {
            return segmentObject.toByteArray();
        } else {
            return segmentBuilder.build().toByteArray();
        }
    }

    @Override public void toBuilder() {
        if (isOrigin) {
            this.isOrigin = false;
            this.segmentBuilder = segmentObject.toBuilder();
        }
    }
}
