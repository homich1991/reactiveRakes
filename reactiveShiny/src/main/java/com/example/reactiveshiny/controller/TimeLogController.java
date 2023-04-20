package com.example.reactiveshiny.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@RestController
@RequestMapping("/api")
public class TimeLogController {

    private final WebClient webClient;

    public TimeLogController() {
        webClient = WebClient
                .builder()
                .baseUrl("http://localhost:8080/api")
                .build();
    }

    @GetMapping("/hello-time")
    Mono<String> helloWithTime() {
        LocalDateTime before = LocalDateTime.now();
        return webClient
                .get()
                .uri("/hello-delay")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)

                .doOnNext(res ->{
                    LocalDateTime after = LocalDateTime.now();

                    log.info("Time of the call is {} millis\n", ChronoUnit.MILLIS.between(before, after));
                })
                .map(this::mapResult)
                .doOnNext(res ->{
                    LocalDateTime after = LocalDateTime.now();

                    log.info("Time after Map is {} millis\n", ChronoUnit.MILLIS.between(before, after));
                });
    }

    String mapResult(String result) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        return result;
    }

}
