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

    final AtomicLong counter;


    public UpdateGeneratorController() {
        this.counter = new AtomicLong();
        updates = Sinks.many().multicast().directAllOrNothing();
        addNewUpdate();
        addNewUpdate();
        addNewUpdate();
    }

    @GetMapping(value = "/updates", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getUpdates() {
        return updates.asFlux();
    }


    @GetMapping("/add-updates")
    public void setUpdate() {
        addNewUpdate();
    }


    private void addNewUpdate() {
        Sinks.EmitResult result = updates.tryEmitNext("Hello World #" + counter.getAndIncrement());

        if (result.isFailure()) {
            System.out.println("Fail to emit next");
        }
    }


}
