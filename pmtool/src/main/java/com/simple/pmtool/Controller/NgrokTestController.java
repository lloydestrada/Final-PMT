package com.simple.pmtool.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NgrokTestController {

    @GetMapping("/ngrok-test")
    public String test() {
        return "Ngrok test successful!";
    }
}
