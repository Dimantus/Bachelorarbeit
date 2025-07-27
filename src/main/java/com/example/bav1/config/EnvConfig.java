package com.example.bav1.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class EnvConfig {
    private String baseUrl;
    private String completionsPath;
    private String MODEL;
    private String temperature;
    private String max_tokens;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getCompletionsPath() {
        return completionsPath;
    }

    public void setCompletionsPath(String completionsPath) {
        this.completionsPath = completionsPath;
    }

    public String getMODEL() {
        return MODEL;
    }

    public void setMODEL(String MODEL) {
        this.MODEL = MODEL;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getMax_tokens() {
        return max_tokens;
    }

    public void setMax_tokens(String max_tokens) {
        this.max_tokens = max_tokens;
    }
}
