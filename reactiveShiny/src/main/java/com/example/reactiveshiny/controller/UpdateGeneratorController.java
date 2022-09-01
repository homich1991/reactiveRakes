package com.example.reactiveshiny.controller;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/api/reactive")
public class UpdateGeneratorController {

    private void addNewUpdate() {
        throw new NotImplementedException();
    }

    @GetMapping("/add-update")
    public void setUpdate() {
        addNewUpdate();
    }

    @GetMapping(value = "/update", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getUpdates() {
        throw new NotImplementedException();
    }

}
