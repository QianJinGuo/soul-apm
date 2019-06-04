package com.furioussoulk.collector.stream.worker.worker.base;

import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.graph.NodeProcessor;
import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.graph.WayToNode;

public abstract class WorkerRef<INPUT, OUTPUT> extends WayToNode<INPUT, OUTPUT> {
    WorkerRef(NodeProcessor<INPUT, OUTPUT> destinationHandler) {
        super(destinationHandler);
    }
}
