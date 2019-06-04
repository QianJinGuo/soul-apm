package com.furioussoulk.apm.collector.core.agent;

import com.furioussoulk.apm.collector.core.agent.mvc.TestController;

public class Main {
    static TestController testController = new TestController();

    public static void main(String[] args) {
        testController.get("1");
        System.out.println("hello world");
    }
}
