package com.example.bav1.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@org.springframework.web.bind.annotation.RestController
@CrossOrigin(origins = "*")
@RequestMapping("/chat")

public class RestController {


    private final ChatClient chatClient;
    public RestController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping("/mvc")
    public String chat(@RequestBody String input) {
        return chatClient.prompt(input).call().content();
    }

    @PostMapping("/mvc-stream")
    public void stream(HttpServletResponse response, @RequestBody String input) throws IOException {
        response.setContentType("text/plain");
        OutputStream out = response.getOutputStream();

        chatClient.prompt(input).stream().content()
                .doOnNext(token -> {
                    try {
                        out.write(token.getBytes(StandardCharsets.UTF_8));
                        out.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .doOnComplete(() -> {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .doOnError(err -> {
                    try {
                        out.write(("Fehler: " + err.getMessage()).getBytes(StandardCharsets.UTF_8));
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .blockLast();
    }

    @PostMapping("/weblux")
    public Mono<String> weblux_chat(@RequestBody String input) {
        return Mono.just(Objects.requireNonNull(chatClient.prompt(input).call().content()));
    }

    @PostMapping("/webflux-stream")
    public Flux<String> webflux_stream(@RequestBody String input) {
        return chatClient.prompt(input).stream().content();
    }
}
