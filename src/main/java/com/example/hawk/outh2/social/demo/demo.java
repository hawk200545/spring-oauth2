package com.example.hawk.outh2.social.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class demo {
    @GetMapping
    ResponseEntity<String> hello(){
        return ResponseEntity.ok("Hello World");
    }
}
