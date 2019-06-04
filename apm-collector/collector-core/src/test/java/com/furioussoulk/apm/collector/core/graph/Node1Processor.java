
package com.furioussoulk.apm.collector.core.graph;


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
