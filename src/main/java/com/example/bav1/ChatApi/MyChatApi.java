package com.example.bav1.ChatApi;

import org.springframework.ai.model.ChatModelDescription;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;

public class MyChatApi {

    private final ChatModel model;
    private final String baseUrl;
    private final String completionsPath;
    private final RestClient restClient;
    private final WebClient webClient;

    public Builder mutate() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public MyChatApi(ChatModel model, String baseUrl, String completionsPath, RestClient.Builder restClientBuilder, WebClient.Builder webClientBuilder) {

        this.model = model;
        this.baseUrl = baseUrl;
        this.completionsPath = completionsPath;

        URI completeUrl = URI.create(this.baseUrl + this.completionsPath);

        System.out.println("Die komplette URL lautet: " + completeUrl);

        System.out.println("Model: " + model.getName() + ", baseUrl: " + baseUrl + ", completionsPath: " + completionsPath + ", completeUrl: " + completeUrl);

        this.restClient = restClientBuilder
                .baseUrl(completeUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();

        this.webClient = webClientBuilder
                .baseUrl(completeUrl.toString())
                .defaultHeader("Content-Type", "application/json")
                .build();
    }


    public ChatModel getMODEL() {
        return model;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public String getCompletionsPath() {
        return this.completionsPath;
    }

    public RestClient getRestClient() {
        return this.restClient;
    }

    public WebClient getWebClient() {
        return this.webClient;
    }


    public static enum ChatModel implements ChatModelDescription {
        MISTRAL7B("mistral-7b-instruct-v0.3"),
        LLAMA("TheBloke/Llama-2-7b-Chat-AWQ"),
        TINYLLAMA("TheBloke/TinyLlama-1.1B-Chat-v1.0-AWQ");

        public final String value;

        private ChatModel(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public String getName() {
            return this.value;
        }
    }


    public static class Builder {

        private ChatModel model;
        private String baseUrl;
        private String completionsPath;
        private RestClient.Builder restClientBuilder;
        private WebClient.Builder webClientBuilder;

        public Builder() {
            this.model = ChatModel.MISTRAL7B;
            this.baseUrl = "http://localhost:1234";
            this.completionsPath = "/api/v0/chat/completions";
            this.restClientBuilder = RestClient.builder();
            this.webClientBuilder = WebClient.builder();
        }

        public Builder(MyChatApi api) {
            this.model = api.getMODEL();
            this.baseUrl = api.getBaseUrl();
            this.completionsPath = api.getCompletionsPath();
            this.restClientBuilder = api.restClient != null ? api.restClient.mutate() : RestClient.builder();
            this.webClientBuilder = api.webClient != null ? api.webClient.mutate() : WebClient.builder();
        }

        public Builder model(String Model) {
            Assert.hasText(Model, "model cannot be null or empty");
            this.model = ChatModel.valueOf(Model.trim());
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            Assert.hasText(baseUrl, "baseUrl cannot be null or empty");
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder completionsPath(String completionsPath) {
            Assert.hasText(completionsPath, "completionsPath cannot be null or empty");
            this.completionsPath = completionsPath;
            return this;
        }

        public Builder restClientBuilder(RestClient.Builder restClientBuilder) {
            Assert.notNull(restClientBuilder, "restClientBuilder cannot be null");
            this.restClientBuilder = restClientBuilder;
            return this;
        }

        public Builder webClientBuilder(WebClient.Builder webClientBuilder) {
            Assert.notNull(webClientBuilder, "webClientBuilder cannot be null");
            this.webClientBuilder = webClientBuilder;
            return this;
        }

        public MyChatApi build() {
            return new MyChatApi(this.model, this.baseUrl, this.completionsPath, this.restClientBuilder, this.webClientBuilder);
        }
    }
}

