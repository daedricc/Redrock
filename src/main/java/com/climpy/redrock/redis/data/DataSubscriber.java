package com.climpy.redrock.redis.data;

import com.climpy.redrock.RedrockPlugin;
import com.google.gson.JsonObject;
import lombok.Getter;

@Getter
public abstract class DataSubscriber {
    private final RedrockPlugin redrockPlugin;
    private String registeredChannel;

    public String getRegisteredChannel() {
        return this.registeredChannel;
    }

    public DataSubscriber(RedrockPlugin redrockPlugin, String registeredChannel) {
        this.redrockPlugin = redrockPlugin;
        this.registeredChannel = registeredChannel;
    }

    public abstract void handleMessage(JsonObject paramJsonObject);
}
