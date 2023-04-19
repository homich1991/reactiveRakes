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
        return "Hello from old MVC";
    }

    @PostMapping("/helloPost")
    public String helloPost(@RequestBody PostRequestBody body) {
        return "Hello " + body.name();
    }

    @GetMapping("/hello-delay")
    public String helloDelay() throws InterruptedException {
        Thread.sleep(new Random().nextInt(500, 1000));
        return "Hello from old MVC with delay";
    }

    @GetMapping("/fallback")
    public String fallback() {
        return "Fallback procedure happened";
    }

    @GetMapping("/error")
    public ResponseEntity<String> error() {
        return ResponseEntity.badRequest().body("Something awful happened");
    }

    @GetMapping("/error1")
    public ResponseEntity<String> error1() {
        return ResponseEntity.notFound().build();
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
