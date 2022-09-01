package com.example.reactiveshiny.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
                    } else {
                        log.error("Error happened {}", response);
                        return response.bodyToMono(String.class)
                                .flatMap(error ->
                                        Mono.error(new MyAnotherCustomException("Custom:" + error, null)));
                    }
                });
    }

    @GetMapping("/logBlock")
    Mono<String> helloWithLogBlock() {
        return webClient
                .get()
                .uri("/error")
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(response -> {
                    if (response.statusCode() == HttpStatus.OK) {
                        return response.bodyToMono(String.class);
                    } else {
                        String str = response.bodyToMono(String.class)
                                .publishOn(Schedulers.boundedElastic())
                                .block();
                        log.error("Error happened {}", str);
                        return response.bodyToMono(String.class)
                                .flatMap(error ->
                                        Mono.error(new MyAnotherCustomException("Custom:" + error, null)));
                    }
                });
    }

    @GetMapping("/logSubscribe")
    Mono<String> helloWithLogSubscribe() {
        return webClient
                .get()
                .uri("/errorObject")
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(response -> {
                    if (response.statusCode() == HttpStatus.OK) {
                        return response.bodyToMono(String.class);
                    } else {
                        response.bodyToMono(String.class)
                                .publishOn(Schedulers.boundedElastic())
                                .subscribe(resp -> log.error("Error happened {}", resp));
                        return response.bodyToMono(ErrorObject.class).flatMap(resp -> Mono.error(new MyAnotherCustomException("Custom:" + resp.name, null)));
                    }
                });
    }


    @GetMapping("/logMap")
    Mono<String> helloWithLogMap() {
        return webClient
                .get()
                .uri("/error")
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(response -> {
                    if (response.statusCode() == HttpStatus.OK) {
                        return response.bodyToMono(String.class);
                    } else {
                        return response.bodyToMono(String.class)
                                .map(resp -> {
                                    log.error("Error happened {}", resp);
                                    return Mono.just(resp);
                                })
                                .flatMap(resp -> Mono.error(new MyAnotherCustomException("Custom:" + resp, null)));
                    }
                });
    }

    @GetMapping("/logDoOnNext")
    Mono<String> helloWithLogDoOnNext() {
        return webClient
                .get()
                .uri("/error")
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(response -> {
                    if (response.statusCode() == HttpStatus.OK) {
                        return response.bodyToMono(String.class);
                    } else {
                        return response.bodyToMono(String.class)
                                .doOnNext(resp -> log.error("Error happened {}", resp))
                                .flatMap(resp -> Mono.error(new MyAnotherCustomException("Custom:" + resp, null)));
                    }
                });
    }

    record ErrorObject(String name) {
    }

    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    class MyCustomException extends RuntimeException {
        public MyCustomException(String msg, Throwable e) {
            super(msg, e);
        }
    }

    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    class MyAnotherCustomException extends RuntimeException {
        public MyAnotherCustomException(String msg, Throwable e) {
            super(msg, e);
        }
    }


}
