package com.example.mimickimodell;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v1/chat")
public class RestControllerMimicKi {

    private static final String[] WORDS = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt.".split(" ");

    @PostMapping(value = "/completions", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<JsonModel> chat(@RequestBody RequestModel body) {

        return Flux.range(0, WORDS.length)
                .map(i -> new JsonModel(
                        WORDS[i] + " ",
                        (i == WORDS.length - 1) ? "stop" : null
                ))
                .delayElements(Duration.ofMillis(200));
    }
}
