package com.example.hawk.outh2.social.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class root {
    @GetMapping
    ResponseEntity<String> helloAll(){
        return ResponseEntity.ok("Im Hawk, you just passed the oauth2 login...");
    }
}
