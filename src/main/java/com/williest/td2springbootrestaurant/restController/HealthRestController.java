package com.williest.td2springbootrestaurant.restController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthRestController {
    @GetMapping("/ping")
    public ResponseEntity<String> pong() {
        String responseBody = "pong";
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseBody);
    }
}
