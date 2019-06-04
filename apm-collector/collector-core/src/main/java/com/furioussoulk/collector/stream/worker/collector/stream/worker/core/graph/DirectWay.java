package com.furioussoulk.collector.stream.worker.collector.stream.worker.core.graph;

/**
 *
 * @author 孙证杰
 * @date 2019/6/3 13:30
 */
public class DirectWay<INPUT, OUTPUT> extends WayToNode<INPUT, OUTPUT> {
    public DirectWay(NodeProcessor<INPUT, OUTPUT> destinationHandler) {
        super(destinationHandler);
    }

    @Override protected void in(INPUT o) {
        out(o);
    }
}
