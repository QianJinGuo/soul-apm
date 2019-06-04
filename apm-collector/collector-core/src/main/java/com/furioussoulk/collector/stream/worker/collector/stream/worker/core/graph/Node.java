package com.furioussoulk.collector.stream.worker.collector.stream.worker.core.graph;

/**
 * The <code>Node</code> in the graph with explicit INPUT and OUTPUT types.
 *
 * @author 孙证杰
 * @date 2019/6/3 13:19
 */
public final class Node<INPUT,OUTPUT> {

    private final NodeProcessor nodeProcessor;
    private final Next<OUTPUT> next;
    private final Graph graph;

    Node(Graph graph, NodeProcessor<INPUT, OUTPUT> nodeProcessor) {
        this.graph = graph;
        this.nodeProcessor = nodeProcessor;
        this.next = new Next<>();
        this.graph.checkForNewNode(this);
    }

    public final <NEXTOUTPUT> Node<OUTPUT, NEXTOUTPUT> addNext(NodeProcessor<OUTPUT, NEXTOUTPUT> nodeProcessor) {
        return this.addNext(new DirectWay(nodeProcessor));
    }

    public final <NEXTOUTPUT> Node<OUTPUT, NEXTOUTPUT> addNext(WayToNode<OUTPUT, NEXTOUTPUT> way) {
        synchronized (graph) {
            way.buildDestination(graph);
            next.addWay(way);
            return way.getDestination();
        }
    }

    final void execute(INPUT input) {
        nodeProcessor.process(input, next);
    }

    NodeProcessor getHandler() {
        return nodeProcessor;
    }

    Next<OUTPUT> getNext() {
        return next;
    }
}
