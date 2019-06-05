package com.furioussoulk.collector.agent.jetty.reader;

import com.furioussoulk.network.proto.TraceSegmentObject;
import com.furioussoulk.network.proto.UniqueId;
import com.furioussoulk.network.proto.UpstreamSegment;

public class TraceSegment {

    private UpstreamSegment.Builder builder;

    public TraceSegment() {
        builder = UpstreamSegment.newBuilder();
    }

    public void addGlobalTraceId(UniqueId.Builder globalTraceId) {
        builder.addGlobalTraceIds(globalTraceId);
    }

    public void setTraceSegmentBuilder(TraceSegmentObject.Builder traceSegmentBuilder) {
        builder.setSegment(traceSegmentBuilder.build().toByteString());
    }

    public UpstreamSegment getUpstreamSegment() {
        return builder.build();
    }
}
