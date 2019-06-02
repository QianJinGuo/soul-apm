package com.furioussoulk;

import com.furioussoulk.mvc.TestController;
import org.springframework.stereotype.Controller;

@Controller
public class Main {
    static TestController testController = new TestController();

    public static void main(String[] args) {
        testController.get("1");
        System.out.println("hello world");
    }
}
