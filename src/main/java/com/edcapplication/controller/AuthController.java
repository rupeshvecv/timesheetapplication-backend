package com.edcapplication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/api/public/hello")
    public String publicHello() {
        return "Hello (no token needed)";
    }

    @GetMapping("/api/private/hello")
    public String privateHello() {
        return "Hello (secured endpoint)";
    }
}
