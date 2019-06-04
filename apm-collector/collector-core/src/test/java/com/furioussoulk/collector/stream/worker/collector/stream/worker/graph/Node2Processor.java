
package com.furioussoulk.collector.stream.worker.collector.stream.worker.graph;


public class Node2Processor extends OutputProcessor implements NodeProcessor<String, Integer> {
    @Override
    public int id() {
        return 2;
    }

    @Override
    public void process(String s, Next<Integer> next) {
        outstream().println("Node2 process: s=" + s);
        next.execute(123);
    }
}
