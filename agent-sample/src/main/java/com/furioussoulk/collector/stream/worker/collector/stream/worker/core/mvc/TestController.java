package com.furioussoulk.collector.stream.worker.collector.stream.worker.core.mvc;

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
