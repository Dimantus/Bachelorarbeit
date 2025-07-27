package com.example.mimickimodell;

import java.util.List;
import java.util.UUID;

public class JsonModel {
    private String id = "1";
    private String object = "chat.completion.chunk";
    private Integer created = 1234;
    private String model = "random-model";
    private String system_fingerprint = "random-fingerprint";
    private List<Choices> choices;

    public JsonModel(String word, String finish_reason) {
        this.choices = List.of(new Choices(new Delta(word), finish_reason));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSystem_fingerprint() {
        return system_fingerprint;
    }

    public void setSystem_fingerprint(String system_fingerprint) {
        this.system_fingerprint = system_fingerprint;
    }

    public List<Choices> getChoices() {
        return choices;
    }

    public void setChoices(List<Choices> choices) {
        this.choices = choices;
    }

    public class Choices{
        private int index = 0;
        private Delta delta;
        private String logprobs = null;
        private String finish_reason = null;

        public Choices(Delta delta, String finish_reason) {
            this.delta = delta;
            this.finish_reason = finish_reason;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public Delta getDelta() {
            return delta;
        }

        public void setDelta(Delta delta) {
            this.delta = delta;
        }

        public String getFinish_reason() {
            return finish_reason;
        }

        public void setFinish_reason(String finish_reason) {
            this.finish_reason = finish_reason;
        }
    }

    public class Delta{
        private String role = "assistant";
        private String content;

        public Delta(String content) {
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}

