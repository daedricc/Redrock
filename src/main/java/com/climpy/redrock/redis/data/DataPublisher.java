package com.climpy.redrock.redis.data;

import com.climpy.redrock.RedrockPlugin;
import com.climpy.redrock.utils.ConfigUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import redis.clients.jedis.Jedis;

import java.util.logging.Level;

public class DataPublisher {
    private final RedrockPlugin redrockPlugin;

    public DataPublisher(RedrockPlugin redrockPlugin) {
        this.redrockPlugin = redrockPlugin;
    }

    public void write(String channel, String message) {
        try (Jedis jedis = RedrockPlugin.getInstance().getJedisPool().getResource()) {
            ConfigUtils configRedis = new ConfigUtils(RedrockPlugin.getInstance().getMainConfig(), "redis");
            jedis.auth(configRedis.getString("password"));
            jedis.publish(channel, message);
        } catch (Exception ex) {
            this.redrockPlugin.getLogger().log(Level.WARNING, ex.getMessage());
        }
    }

    public void write(String channel, JsonObject jsonObject) {
        try (Jedis jedis = RedrockPlugin.getInstance().getJedisPool().getResource()) {
            ConfigUtils configRedis = new ConfigUtils(RedrockPlugin.getInstance().getMainConfig(), "redis");
            jedis.auth(configRedis.getString("password"));
            String jsonString = (new Gson()).toJson((JsonElement) jsonObject);
            jedis.publish(channel, jsonString);
        } catch (Exception ex) {
            this.redrockPlugin.getLogger().log(Level.WARNING, ex.getMessage());
        }
    }
}
