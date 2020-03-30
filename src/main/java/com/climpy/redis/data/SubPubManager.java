package com.climpy.redis.data;

import com.climpy.RedrockPlugin;
import com.climpy.redis.messages.*;
import com.climpy.utils.ConfigUtils;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class SubPubManager {
    private final RedrockPlugin redrockPlugin;

    private Set<DataSubscriber> subscribers;

    public SubPubManager(RedrockPlugin redrockPlugin) {
        this.redrockPlugin = redrockPlugin;
        this.subscribers = new HashSet<>();
        this.subscribers.add(new StaffSubscriber());
        this.subscribers.add(new ChatSubscriber());
        this.subscribers.add(new MessageSubscriber());
        this.subscribers.add(new AlertSubscriber());
        this.subscribers.add(new ReportSubscriber());
        this.subscribers.add(new RequestSubscriber());
        this.subscribers.add(new ServerDataSubscriber());
        this.subscribers.add(new WhitelistSubscriber());
        this.subscribers.add(new PunishmentSubscriber());
        this.subscribers.add(new PunishmentClearSubscricber());
        this.subscribers.add(new StreamingSubscriber());
        this.subscribers.add(new AddGrantSubsricber());
        this.subscribers.add(new DeleteGrantSubsricber());
    }

    public void createConnection() {
        try {
            if (RedrockPlugin.getInstance().getJedisPubSub() != null)
                return;
            this.redrockPlugin.getLogger().log(Level.INFO, "Sub-Pub şu anda kuruluyor..");
            ConfigUtils configRedis = new ConfigUtils(RedrockPlugin.getInstance().getMainConfig(), "redis");
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            String redisServer = configRedis.getString("server");
            int redisPort = configRedis.getInt("port");
            String redisPassword = configRedis.getString("password");
            jedisPoolConfig.setMaxTotal(this.redrockPlugin.getMainConfig().getFileConfiguration().getInt("max-redis-connections"));
            JedisPool jedisPool = new JedisPool((GenericObjectPoolConfig) jedisPoolConfig, redisServer, redisPort, 0, redisPassword);
            (RedrockPlugin.getInstance()).jedisPubSub = getJedisPubSub();
            try {
                Jedis jedis = jedisPool.getResource();
                jedis.auth(redisPassword);
                String[] stringArray = new String[this.subscribers.size()];
                for (int i = 0; i < stringArray.length; ) {
                    for (DataSubscriber subscriber : this.subscribers) {
                        this.redrockPlugin.getLogger().log(Level.INFO, "[" + i + "] - " + subscriber.getRegisteredChannel() + " kanalı isteme geçiyor.");
                        stringArray[i++] = subscriber.getRegisteredChannel();
                    }
                }
                try {
                    Thread pubSubThread = new Thread(() -> jedis.subscribe((RedrockPlugin.getInstance()).jedisPubSub, stringArray));
                    pubSubThread.setName("Sub-Pub-Task");
                    pubSubThread.start();
                } catch (Exception ex) {
                    this.redrockPlugin.getLogger().log(Level.WARNING, ex.getMessage());
                }
                if (RedrockPlugin.getInstance().getJedisPubSub() == null) {
                    this.redrockPlugin.getLogger().log(Level.WARNING, "Jedis Sub-Pub verileri anda bodurumda.");
                } else {
                    this.redrockPlugin.getLogger().log(Level.INFO, "Sub-Pub bakuruldu, babekleniyor!");
                    RedrockPlugin.getInstance().setEnableRedisPubSub(true);
                }
            } catch (IndexOutOfBoundsException | NullPointerException ex) {
                this.redrockPlugin.getLogger().log(Level.WARNING, "Sub-Pub kurulumu sbir hata oluştu.");
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            this.redrockPlugin.getLogger().log(Level.SEVERE, "Sub-Pub kurulumu sbilinmeyen hata.");
            ex.printStackTrace();
        }
    }

    private JedisPubSub getJedisPubSub() {
        return new JedisPubSub() {
            public void onMessage(String channel, String message) {
                for (DataSubscriber dataSubscriber : SubPubManager.this.subscribers) {
                    String regChannel = dataSubscriber.getRegisteredChannel();
                    if (regChannel.equals(channel)) {
                        JsonReader jsonReader = new JsonReader(new StringReader(message));
                        dataSubscriber.handleMessage((new JsonParser()).parse(jsonReader).getAsJsonObject());
                    }
                }
            }
        };
    }
}
