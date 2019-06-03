
package com.furioussoulk.graph;

import com.furioussoulk.exception.CollectorException;
import com.furioussoulk.exception.NodeNotFoundException;
import com.furioussoulk.exception.PotentialCyclicGraphException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class GraphManagerTest {
    private static PrintStream OUT_REF;
    private static String LINE_SEPARATE = System.lineSeparator();
    private ByteArrayOutputStream outputStream;

    @Before
    public void initAndHoldOut() {
        OUT_REF = System.out;
        outputStream = new ByteArrayOutputStream();
        PrintStream testStream = new PrintStream(outputStream);
        System.setOut(testStream);
    }

    @After
    public void reset() {
        System.setOut(OUT_REF);
    }

    @Test
    public void testGraph() {
        Graph<String> testGraph = GraphManager.INSTANCE.createIfAbsent(1);
        Node<String, String> node = testGraph.addNode(new Node1Processor());
        Node<String, Integer> node1 = node.addNext(new Node2Processor());
        testGraph.start("Input String");

        String output = outputStream.toString();
        String expected = "Node1 process: s=Input String" + LINE_SEPARATE +
                "Node2 process: s=Input String" + LINE_SEPARATE;

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testGraphWithChainStyle() {
        Graph<String> graph = GraphManager.INSTANCE.createIfAbsent(2);
        graph.addNode(new Node1Processor()).addNext(new Node2Processor()).addNext(new Node4Processor());

        graph.start("Input String");

        String output = outputStream.toString();
        String expected = "Node1 process: s=Input String" + LINE_SEPARATE +
                "Node2 process: s=Input String" + LINE_SEPARATE +
                "Node4 process: int=123" + LINE_SEPARATE;

        Assert.assertEquals(expected, output);
    }

    @Test(expected = PotentialCyclicGraphException.class)
    public void testPotentialAcyclicGraph() {
        Graph<String> testGraph = GraphManager.INSTANCE.createIfAbsent(3);
        Node<String, String> node = testGraph.addNode(new Node1Processor());
        node.addNext(new Node1Processor());
    }

    @Test
    public void testContinueStream() throws CollectorException {
        Graph<String> graph = GraphManager.INSTANCE.createIfAbsent(4);
        graph.addNode(new Node1Processor()).addNext(new Node2Processor()).addNext(new Node4Processor());

        Next next = GraphManager.INSTANCE.findGraph(4).toFinder().findNext(2);

        next.execute(123);
        String output = outputStream.toString();
        String expected =
                "Node4 process: int=123" + LINE_SEPARATE;

        Assert.assertEquals(expected, output);
    }

    @Test(expected = NodeNotFoundException.class)
    public void handlerNotFound() {
        Graph<String> graph = GraphManager.INSTANCE.createIfAbsent(5);
        graph.addNode(new Node1Processor()).addNext(new Node2Processor()).addNext(new Node4Processor());

        Next next = GraphManager.INSTANCE.findGraph(5).toFinder().findNext(3);
    }

    @Test
    public void testFindNode() {
        Graph<String> graph = GraphManager.INSTANCE.createIfAbsent(6);
        graph.addNode(new Node1Processor()).addNext(new Node2Processor());

        Node<?, Integer> foundNode = GraphManager.INSTANCE.findGraph(6).toFinder().findNode(2, Integer.class);
        foundNode.addNext(new Node4Processor());
    }

    @Test
    public void testDeadEndWay() {
        Graph<String> graph = GraphManager.INSTANCE.createIfAbsent(7);
        graph.addNode(new Node1Processor()).addNext(new WayToNode<String, Integer>(new Node2Processor()) {
            @Override
            protected void in(String input) {
                //don't call `out(intput)`;
            }
        });

        graph.start("Input String");
        String output = outputStream.toString();
        String expected = "Node1 process: s=Input String" + LINE_SEPARATE;

        Assert.assertEquals(expected, output);
    }

    @Test
    public void testEntryWay() {
        Graph<String> graph = GraphManager.INSTANCE.createIfAbsent(8);
        graph.addNode(new WayToNode<String, String>(new Node1Processor()) {
            @Override
            protected void in(String input) {
                //don't call `out(intput)`;
            }
        }).addNext(new Node2Processor());

        graph.start("Input String");

        Assert.assertEquals("", outputStream.toString());
    }

    @After
    public void tearDown() {
        GraphManager.INSTANCE.reset();
    }
}
