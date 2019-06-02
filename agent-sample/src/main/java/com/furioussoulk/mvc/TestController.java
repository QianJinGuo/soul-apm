package com.furioussoulk.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Controller
public class TestController {


    @RequestMapping("test")
    public Map get(String id) {
        return null;
    }
}
