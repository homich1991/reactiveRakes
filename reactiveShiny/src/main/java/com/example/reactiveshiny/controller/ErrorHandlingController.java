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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@RestController
@RequestMapping("/api")
public class ErrorHandlingController {

    private final WebClient webClient;

    public ErrorHandlingController() {
        webClient = WebClient
                .builder()
                .baseUrl("http://localhost:8080/api")
                .build();
    }


//    Error handling

    @GetMapping("/error")
    Mono<ResponseEntity<String>> errorForward() {
        return webClient
                .get()
                .uri("/error")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(String.class);
    }


    @GetMapping("/error1")
    Mono<ResponseEntity<String>> errorStatusForward() {
        return webClient
                .get()
                .uri("/error")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::isError, this::errorHandler)
                .toEntity(String.class);
    }


    private Mono<? extends Throwable> errorHandler(ClientResponse clientResponse) {
        var statusCode = clientResponse.statusCode();
        var errorResponseEntity = clientResponse.toEntity(String.class);

        if (statusCode == HttpStatus.NOT_FOUND) {
            return Mono.error(new MyCustomException("NotFound", null));
        }

        return errorResponseEntity
                .map(error -> new MyCustomException("Custom status: " + error.getBody(), null));
    }

    @GetMapping("/error2")
    Mono<ResponseEntity<String>> errorMapForward() {
        return webClient
                .get()
                .uri("/error")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::isError, this::errorHandler)
                .toEntity(String.class)
                .onErrorMap(this::mapError);
    }

    private Throwable mapError(Throwable e) {
        return (e instanceof MyCustomException) ? new MyAnotherCustomException("Another Custom status", e) : e;
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
