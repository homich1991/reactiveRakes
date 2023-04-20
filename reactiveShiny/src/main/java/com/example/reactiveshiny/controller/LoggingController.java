package com.example.reactiveshiny.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api")
public class LoggingController {

    private final WebClient webClient;

    public LoggingController() {
        webClient = WebClient
                .builder()
                .baseUrl("http://localhost:8080/api")
                .build();
    }


    @GetMapping("/log")
    Mono<String> helloWithLog() {
        return webClient
                .get()
                .uri("/error")
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(response -> {
                    if (response.statusCode() == HttpStatus.OK) {
                        return response.bodyToMono(String.class);
                    }
                    return response.bodyToMono(String.class)
                            .doOnNext(resp->log.error("Error happened {}", resp));

                });
    }


}
