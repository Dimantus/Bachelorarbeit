package com.example.bav1.config;


import com.example.bav1.ChatApi.MyChatApi;
import com.example.bav1.ChatModel.MyChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig{

    @Bean
    public MyChatApi myChatApi(EnvConfig envConfig) {

        System.out.println("Model " + envConfig.getMODEL() + ", Baseurl " + envConfig.getBaseUrl() + ", CompletionPath " + envConfig.getCompletionsPath());

        MyChatApi.Builder builder = MyChatApi.builder();

        String model = envConfig.getMODEL();
        String baseUrl = envConfig.getBaseUrl();
        String completionsPath = envConfig.getCompletionsPath();

        if (model != null && !model.isBlank()) {
            builder.model(model);
        }
        if (baseUrl != null && !baseUrl.isBlank()) {
            builder.baseUrl(baseUrl);
        }
        if (completionsPath != null && !completionsPath.isBlank()) {
            builder.completionsPath(completionsPath);
        }

        return builder.build();

    }

    @Bean
    public MyChatModel myChatModel(MyChatApi myChatApi, EnvConfig envConfig) {
        return new MyChatModel(myChatApi, myChatApi.getMODEL(), envConfig.getTemperature(), envConfig.getMax_tokens());
    }

    @Bean
    public ChatClient chatClient(MyChatModel myChatModel) {
        return ChatClient.create(myChatModel);
    }
}
