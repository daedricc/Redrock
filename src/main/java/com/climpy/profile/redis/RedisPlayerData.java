package com.climpy.profile.redis;

import com.climpy.utils.TimeUtils;
import com.google.gson.JsonObject;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Getter
@Data
public class RedisPlayerData {
    private final UUID uniqueUUID;
    private final String name;

    private RedisPlayerAction redisPlayerAction;
    private String lastSeenServer;
    private long lastSeenAt;

    public RedisPlayerData(UUID uniqueUUID, String name) {
        this.uniqueUUID = uniqueUUID;
        this.name = name;
    }

    public RedisPlayerData(JsonObject object) {
        this.uniqueUUID = UUID.fromString(object.get("uniqueUUID").getAsString());
        this.name = object.get("name").getAsString();

        this.redisPlayerAction = RedisPlayerAction.getRedisPlayerAction(object.get("redisPlayerAction").getAsString());
        this.lastSeenServer = object.get("lastSeenServer").getAsString();
        this.lastSeenAt = object.get("lastSeenAt").getAsLong();
    }

    public JsonObject getJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uniqueUUID", this.uniqueUUID.toString());
        jsonObject.addProperty("name", this.name);

        jsonObject.addProperty("redisPlayerAction", this.redisPlayerAction.getInternalName());
        jsonObject.addProperty("lastSeenServer", this.lastSeenServer);
        jsonObject.addProperty("lastSeenAt", this.lastSeenAt);
        return jsonObject;
    }

    public String getLastSeenTimeAgo() {
        return TimeUtils.getDurationWords(System.currentTimeMillis() - this.lastSeenAt, true, true) + " önce";
    }
}
