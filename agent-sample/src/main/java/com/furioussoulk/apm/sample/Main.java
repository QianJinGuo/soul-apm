package com.furioussoulk.apm.sample;

public class Main {
    static TestController testController = new TestController();

    public static void main(String[] args) {
        testController.get("1");
        System.out.println("hello world");
    }
}
