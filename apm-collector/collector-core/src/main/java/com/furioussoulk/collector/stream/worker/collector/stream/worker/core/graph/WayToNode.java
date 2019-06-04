package com.furioussoulk.collector.stream.worker.collector.stream.worker.core.graph;

/**
 *
 * @author 孙证杰
 * @date 2019/6/3 13:27
 */
public abstract class WayToNode<INPUT,OUTPUT> {
    private Node destination;
    private NodeProcessor<INPUT, OUTPUT> destinationHandler;

    public WayToNode(NodeProcessor<INPUT, OUTPUT> destinationHandler) {
        this.destinationHandler = destinationHandler;
    }

    void buildDestination(Graph graph) {
        destination = new Node(graph, destinationHandler);
    }

    protected abstract void in(INPUT input);

    protected void out(INPUT input) {
        destination.execute(input);
    }

    Node getDestination() {
        return destination;
    }
}
