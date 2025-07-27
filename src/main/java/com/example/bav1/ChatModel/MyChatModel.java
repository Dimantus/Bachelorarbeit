package com.example.bav1.ChatModel;

import com.example.bav1.ChatApi.MyChatApi;
import com.example.bav1.ChatApi.MyChatApiResponse;
import com.example.bav1.ChatApi.MyChatApiResponseChunk;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.model.Generation;

import reactor.core.publisher.Flux;

import java.util.*;

public class MyChatModel implements ChatModel {

    private final MyChatApi myChatApi;
    private final MyChatApi.ChatModel chatModel;
    private final String temperatur;
    private final String max_tokens;

    public MyChatModel(MyChatApi myChatApi, MyChatApi.ChatModel chatModel, String temperatur, String max_tokens) {
        this.myChatApi = myChatApi;
        this.chatModel = chatModel;
        this.temperatur = (temperatur != null && !temperatur.isBlank()) ? temperatur : "0.7";
        this.max_tokens = (max_tokens != null && !max_tokens.isBlank()) ? max_tokens : "1000";
    }

    @Override
    public ChatResponse call(Prompt prompt) {

        List<Message> messages = prompt.getInstructions();

        String modelName = chatModel.getValue();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", modelName);
        requestBody.put("temperature", Double.parseDouble(temperatur));
        requestBody.put("max_tokens", Double.parseDouble(max_tokens));
        requestBody.put("stream", false);

        List<Map<String, String>> userMessage = messages.stream().map(msg -> Map.of("role", "user", "content", msg.getText())).toList();
        List<Map<String, String>> apiMessages = new ArrayList<>(userMessage);

        Map<String, String> systemMessage = Map.of("role", "system", "content", "You are a helpful assistant and your answers are very short at most 2 sentences long.");
        apiMessages.add(systemMessage);

        requestBody.put("messages", apiMessages);

        MyChatApiResponse response = myChatApi.getRestClient().post().body(requestBody).retrieve().body(MyChatApiResponse.class);

        String assistantContent = response.getChoices().get(0).getMessage().getContent();

        AssistantMessage assistantMessage = new AssistantMessage(assistantContent);
        Generation generation = new Generation(assistantMessage);

        return new ChatResponse(List.of(generation));
    }

    @Override
    public Flux<ChatResponse> stream(Prompt prompt) {

        List<Message> messages = prompt.getInstructions();

        String modelName = chatModel.getValue();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", modelName);
        requestBody.put("temperature", temperatur);
        requestBody.put("max_tokens", max_tokens);
        requestBody.put("stream", true);

        List<Map<String, String>> apiMessages = messages.stream()
                .map(msg -> Map.of(
                        "role", "user",
                        "content", msg.getText()
                ))
                .toList();

        requestBody.put("messages", apiMessages);

        return myChatApi.getWebClient()
                .post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToFlux(MyChatApiResponseChunk.class)
                .takeUntil(chunk ->
                        chunk.getChoices().get(0).getFinish_reason() != null &&
                                chunk.getChoices().get(0).getFinish_reason().equals("stop")
                )
                .map(chunk -> {
                    String content = chunk.getChoices().get(0).getDelta() != null
                            ? chunk.getChoices().get(0).getDelta().getContent()
                            : "";

                    AssistantMessage assistantMessage = new AssistantMessage(content);
                    Generation generation = new Generation(assistantMessage);

                    return new ChatResponse(List.of(generation));
                });
    }
}

