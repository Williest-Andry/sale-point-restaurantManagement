package com.williest.td2springbootrestaurant.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthRestController {
    @GetMapping("/ping")
    public String pong() {
        return "pong";
    }
}
