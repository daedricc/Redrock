package com.climpy.redrock.redis.data;

import com.climpy.redrock.RedrockPlugin;
import com.climpy.redrock.redis.messages.*;
import com.climpy.redrock.utils.ConfigUtils;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        this.subscribers.add(new MessageSubscriber());
        this.subscribers.add(new AlertSubscriber());
        this.subscribers.add(new ReportSubscriber());
        this.subscribers.add(new RequestSubscriber());
        this.subscribers.add(new ServerDataSubscriber());
        this.subscribers.add(new WhitelistSubscriber());
        this.subscribers.add(new PunishmentSubscriber());
        this.subscribers.add(new PunishmentClearSubscricber());
        this.subscribers.add(new CreateRankSubscriber());
        this.subscribers.add(new DeleteRankSubscriber());
    }

    public void createConnection() {
        try {
            if (RedrockPlugin.getInstance().getJedisPubSub() != null)
                return;
            Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[Redrock] [Redis] " + ChatColor.WHITE + "Sub-Pub su anda kuruluyor..");
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
                        Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[Redrock] [Redis] " + ChatColor.AQUA + "[" + i + "] - " + ChatColor.GRAY + subscriber.getRegisteredChannel() + ChatColor.WHITE + " kanali isteme geciyor.");
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
                    Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[Redrock] [Redis]" + ChatColor.WHITE + " Sub-Pub verileri anda bos durumda.");
                } else {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[Redrock] [Redis]" + ChatColor.WHITE + " Sub-Pub basariyla kuruldu!");
                    RedrockPlugin.getInstance().setEnableRedisPubSub(true);
                }
            } catch (IndexOutOfBoundsException | NullPointerException ex) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[Redrock] [Redis]" + ChatColor.WHITE + " Sub-Pub kurulumu sirasinda bir hata olustu.");
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[Redrock] [Redis]" + ChatColor.WHITE + " Sub-Pub kurulumu sirasinda bilinmeyen hata.");
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
