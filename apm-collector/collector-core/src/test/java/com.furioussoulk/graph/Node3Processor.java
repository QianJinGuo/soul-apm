

package com.furioussoulk.graph;


public class Node3Processor extends OutputProcessor implements NodeProcessor<Long, Long> {
    @Override
    public int id() {
        return 3;
    }

    @Override
    public void process(Long aLong, Next<Long> next) {
        outstream().println("Node3 process: long=" + aLong);
        next.execute(aLong);
    }
}
