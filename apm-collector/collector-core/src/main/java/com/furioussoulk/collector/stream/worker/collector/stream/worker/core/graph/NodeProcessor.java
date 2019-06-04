package com.furioussoulk.collector.stream.worker.collector.stream.worker.core.graph;

/**
 *
 * @author 孙证杰
 * @date 2019/6/3 13:21
 */
public interface NodeProcessor<INPUT,OUTPUT> {
    /**
     * The unique id in the certain graph.
     *
     * @return id
     */
    int id();

    void process(INPUT input, Next<OUTPUT> next);
}
