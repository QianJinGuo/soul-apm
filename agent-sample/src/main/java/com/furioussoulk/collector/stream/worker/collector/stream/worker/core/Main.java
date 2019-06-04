package com.furioussoulk.collector.stream.worker.collector.stream.worker.core;

import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.mvc.TestController;

public class Main {
    static TestController testController = new TestController();

    public static void main(String[] args) {
        testController.get("1");
        System.out.println("hello world");
    }
}
