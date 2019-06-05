package com.furioussoulk.agent.stream.service.trace;

import com.furioussoulk.apm.collector.core.module.Service;
import com.furioussoulk.network.proto.UpstreamSegment;

public interface ITraceSegmentService extends Service {
    void send(UpstreamSegment segment);
}
