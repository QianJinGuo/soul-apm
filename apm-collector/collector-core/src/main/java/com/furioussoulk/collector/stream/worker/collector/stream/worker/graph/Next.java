package com.furioussoulk.collector.stream.worker.collector.stream.worker.graph;

import com.furioussoulk.framework.Executor;

import java.util.LinkedList;
import java.util.List;

/**
 * The <code>Next</code> is a delegate object for the following {@link Node}.
 * @author 孙证杰
 * @date 2019/6/3 13:22
 */
public class Next <INPUT> implements Executor<INPUT> {

    private final List<WayToNode> ways;

    public Next() {
        this.ways = new LinkedList<>();
    }

    final void addWay(WayToNode way) {
        ways.add(way);
    }

    /**
     * Drive to the next nodes
     *
     * @param input
     */
    @Override
    public void execute(INPUT input) {
        ways.forEach(way -> way.in(input));
    }
}
