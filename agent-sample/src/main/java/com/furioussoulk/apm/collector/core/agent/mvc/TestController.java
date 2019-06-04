package com.furioussoulk.apm.collector.core.agent.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("test_controller")
@Controller
public class TestController {

    public TestController() {
        System.out.println("Construct TestController");
    }

    @RequestMapping("test")
    public Map get(String id) {
        return null;
    }
}
