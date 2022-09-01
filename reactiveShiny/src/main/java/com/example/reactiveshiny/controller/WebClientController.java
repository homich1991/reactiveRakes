package com.example.reactiveshiny.controller;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api")
public class WebClientController {

    @GetMapping("/hello")
    Mono<ResponseEntity<String>> helloForward() {
        throw new NotImplementedException();
    }

}
