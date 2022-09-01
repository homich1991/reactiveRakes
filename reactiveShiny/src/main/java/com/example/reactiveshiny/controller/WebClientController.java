package com.example.reactiveshiny.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api")
public class WebClientController {

//    @Autowired
//    private RestTemplate restTemplate;
//
//    @GetMapping("/helloRest")
//    Mono<ResponseEntity<String>> helloForwardRest() {
//        return Mono.just(restTemplate.getForEntity("http://localhost:8080/hello", String.class));
//    }

    private final WebClient webClient;

    public WebClientController() {
        webClient = WebClient
                .builder()
                .baseUrl("http://localhost:8080/api")
                .build();
    }

    @GetMapping("/hello")
    Mono<ResponseEntity<String>> helloForward() {
        return webClient
                .get()
                .uri("/hello")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(String.class);
    }

}
