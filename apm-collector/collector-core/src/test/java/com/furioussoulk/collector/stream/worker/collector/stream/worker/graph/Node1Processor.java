
package com.furioussoulk.collector.stream.worker.collector.stream.worker.graph;


public class Node1Processor extends OutputProcessor implements NodeProcessor<String, String> {
    @Override
    public int id() {
        return 1;
    }

    @Override
    public void process(String s, Next<String> next) {
        outstream().println("Node1 process: s=" + s);
        next.execute(s);
    }
}
