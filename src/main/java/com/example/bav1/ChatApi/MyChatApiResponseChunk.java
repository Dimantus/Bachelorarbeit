package com.example.bav1.ChatApi;

import java.util.List;

public class MyChatApiResponseChunk {

    private String id;
    private String object;
    private long created;
    private String model;
    private String system_fingerprint;
    private List<Choice> choices;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getObject() { return object; }
    public void setObject(String object) { this.object = object; }

    public long getCreated() { return created; }
    public void setCreated(long created) { this.created = created; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getSystem_fingerprint() { return system_fingerprint; }
    public void setSystem_fingerprint(String system_fingerprint) { this.system_fingerprint = system_fingerprint; }

    public List<Choice> getChoices() { return choices; }
    public void setChoices(List<Choice> choices) { this.choices = choices; }


    public static class Choice {

        private int index;
        private Delta delta;
        private String logprobs;
        private String finish_reason;

        public int getIndex() { return index; }
        public void setIndex(int index) { this.index = index; }

        public Delta getDelta() { return delta; }
        public void setDelta(Delta delta) { this.delta = delta; }

        public String getLogprobs() { return logprobs; }
        public void setLogprobs(String logprobs) { this.logprobs = logprobs; }

        public String getFinish_reason() { return finish_reason; }
        public void setFinish_reason(String finish_reason) { this.finish_reason = finish_reason; }
    }

    public static class Delta {

        private String role;
        private String content;

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}
