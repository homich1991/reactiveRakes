package com.example.reactiveshiny.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RestController
@RequestMapping("/api/reactive")
public class UpdateGeneratorController {

    Sinks.Many<String> updates;
    AtomicLong counter;

    public UpdateGeneratorController() {
        this.updates = Sinks.many().multicast().onBackpressureBuffer();
        this.counter = new AtomicLong();
        addNewUpdate();
        addNewUpdate();
        addNewUpdate();
    }

    private void addNewUpdate() {
        long nextNumber = counter.getAndIncrement();
        Sinks.EmitResult result = updates.tryEmitNext("New update # " + nextNumber);
        if (result.isFailure()) {
            log.error("Fail to emit " + nextNumber);
        }

    }

    @GetMapping("/add-update")
    public void setUpdate() {
        addNewUpdate();
    }

    @GetMapping(value = "/update", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getUpdates() {
        return updates.asFlux();
    }

}
