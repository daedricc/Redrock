package com.climpy.profile.redis;

import com.climpy.RedrockPlugin;
import com.google.gson.JsonParser;
import redis.clients.jedis.Jedis;

import java.util.UUID;

public class RedisPlayerCache {

    public RedisPlayerData getRedisPlayerData(UUID uniqueUUID) {
        try (Jedis jedis = RedrockPlugin.getInstance().getJedisPool().getResource()) {
            String jsonObject = jedis.hget("player-data", uniqueUUID.toString());
            if (jsonObject == null) {
                return null;
            }

            try {
                return new RedisPlayerData(new JsonParser().parse(jsonObject).getAsJsonObject());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    public void updateRedisPlayerData(RedisPlayerData redisPlayerData) {
        try (Jedis jedis = RedrockPlugin.getInstance().getJedisPool().getResource()) {
            jedis.hset("player-data", redisPlayerData.getUniqueUUID().toString(), redisPlayerData.getJson().toString());
        }
    }
}
