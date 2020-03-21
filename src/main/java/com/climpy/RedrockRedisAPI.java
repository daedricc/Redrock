package com.climpy;

import com.climpy.utils.ConfigUtils;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.Set;

public class RedrockRedisAPI {
    public static boolean exits(String key) {
        ConfigUtils configRedis = new ConfigUtils(RedrockPlugin.getInstance().getMainConfig(), "redis");
        try (Jedis jedis = RedrockPlugin.getInstance().getJedisPool().getResource()) {
            jedis.auth(configRedis.getString("password"));
            boolean toReturn = jedis.exists(key).booleanValue();
            return toReturn;
        }
    }

    public static void hset(String path, String key, String value) {
        ConfigUtils configRedis = new ConfigUtils(RedrockPlugin.getInstance().getMainConfig(), "redis");
        try (Jedis jedis = RedrockPlugin.getInstance().getJedisPool().getResource()) {
            jedis.auth(configRedis.getString("password"));
            jedis.hset(path, key, value);
        }
    }

    public static void set(String key, String value) {
        ConfigUtils configRedis = new ConfigUtils(RedrockPlugin.getInstance().getMainConfig(), "redis");
        try (Jedis jedis = RedrockPlugin.getInstance().getJedisPool().getResource()) {
            jedis.auth(configRedis.getString("password"));
            jedis.set(key, value);
        }
    }

    public static void hmset(String key, Map<String, String> hash) {
        ConfigUtils configRedis = new ConfigUtils(RedrockPlugin.getInstance().getMainConfig(), "redis");
        try (Jedis jedis = RedrockPlugin.getInstance().getJedisPool().getResource()) {
            jedis.auth(configRedis.getString("password"));
            jedis.hmset(key, hash);
        }
    }

    public static Map<String, String> hgetAll(String key) {
        ConfigUtils configRedis = new ConfigUtils(RedrockPlugin.getInstance().getMainConfig(), "redis");
        try (Jedis jedis = RedrockPlugin.getInstance().getJedisPool().getResource()) {
            jedis.auth(configRedis.getString("password"));
            Map<String, String> toReturn = jedis.hgetAll(key);
            return toReturn;
        }
    }

    public static Set<String> keys(String key) {
        ConfigUtils configRedis = new ConfigUtils(RedrockPlugin.getInstance().getMainConfig(), "redis");
        try (Jedis jedis = RedrockPlugin.getInstance().getJedisPool().getResource()) {
            jedis.auth(configRedis.getString("password"));
            return jedis.keys(key);
        }
    }

    public static void hdel(String key, String... fields) {
        ConfigUtils configRedis = new ConfigUtils(RedrockPlugin.getInstance().getMainConfig(), "redis");
        try (Jedis jedis = RedrockPlugin.getInstance().getJedisPool().getResource()) {
            jedis.auth(configRedis.getString("password"));
            jedis.hdel(key, fields);
        }
    }

    public static void del(String key) {
        ConfigUtils configRedis = new ConfigUtils(RedrockPlugin.getInstance().getMainConfig(), "redis");
        try (Jedis jedis = RedrockPlugin.getInstance().getJedisPool().getResource()) {
            jedis.auth(configRedis.getString("password"));
            jedis.del(key);
        }
    }

    public static void eval(String SCRIPT, String pattern) {
        ConfigUtils configRedis = new ConfigUtils(RedrockPlugin.getInstance().getMainConfig(), "redis");
        try (Jedis jedis = RedrockPlugin.getInstance().getJedisPool().getResource()) {
            jedis.auth(configRedis.getString("password"));
            jedis.eval(String.format(SCRIPT, new Object[] { pattern }));
        }
    }

    public static String hget(String key, String subKey) {
        ConfigUtils configRedis = new ConfigUtils(RedrockPlugin.getInstance().getMainConfig(), "redis");
        try (Jedis jedis = RedrockPlugin.getInstance().getJedisPool().getResource()) {
            jedis.auth(configRedis.getString("password"));
            return jedis.hget(key, subKey);
        }
    }
}
