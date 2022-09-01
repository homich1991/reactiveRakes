package com.example.classicweb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/api")
public class MvcController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }

    @PostMapping("/helloPost")
    public String helloPost(@RequestBody PostRequestBody body) {
        return "Hello " + body.name();
    }

    @GetMapping("/hello-delay")
    public String helloDelay() throws InterruptedException {
        Thread.sleep(new Random().nextInt(1000, 3000));
        return "Hello";
    }

    @GetMapping("/error")
    public ResponseEntity<String> error() {
        return ResponseEntity.badRequest().body("Something awful happened");
    }

    @GetMapping("/errorObject")
    public ResponseEntity<ErrorObject> errorObject() {
        return ResponseEntity.internalServerError().body(new ErrorObject("Something awful happened"));
    }


    record PostRequestBody(String name) {
    }

    record ErrorObject(String name) {
    }
}
