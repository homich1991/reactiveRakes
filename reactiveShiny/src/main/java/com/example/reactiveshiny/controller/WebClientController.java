package com.example.reactiveshiny.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api")
public class WebClientController {

    private final WebClient webClient;

    public WebClientController() {
        webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/api")
                .build();
    }

    @GetMapping("/hello")
    Mono<String> helloForward() {
        return webClient.get()
                .uri("/hello")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);
    }

}
