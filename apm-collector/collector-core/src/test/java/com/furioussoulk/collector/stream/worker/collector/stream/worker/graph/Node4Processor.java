
package com.furioussoulk.collector.stream.worker.collector.stream.worker.graph;

public class Node4Processor extends OutputProcessor implements NodeProcessor<Integer, Long> {
    @Override
    public int id() {
        return 4;
    }

    @Override
    public void process(Integer in, Next<Long> next) {
        outstream().println("Node4 process: int=" + in);
        next.execute(new Long(in.intValue()));
    }
}
