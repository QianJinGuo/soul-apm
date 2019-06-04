package com.furioussoulk.collector.stream.worker.base;

import com.furioussoulk.apm.collector.core.graph.NodeProcessor;
import com.furioussoulk.apm.collector.core.graph.WayToNode;

public abstract class WorkerRef<INPUT, OUTPUT> extends WayToNode<INPUT, OUTPUT> {
    WorkerRef(NodeProcessor<INPUT, OUTPUT> destinationHandler) {
        super(destinationHandler);
    }
}
