package com.climpy.redrock.redis.player;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;

@AllArgsConstructor
@Getter
public enum RedisPlayerAction {
    LEAVING_SERVER("Sunucudan Ayrılıyor", "leaving_server"),
    JOINING_SERVER("Sunucuya Katılıyor", "joining_server");

    private String displayName;
    private String internalName;

    public static RedisPlayerAction getRedisPlayerAction(String internalName) {
        return RedisPlayerAction.valueOf(internalName.toUpperCase(Locale.ENGLISH));
    }
}
