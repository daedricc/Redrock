package com.climpy.redrock.redis;

import com.climpy.redrock.RedrockPlugin;
import com.climpy.redrock.redis.data.DataPublisher;
import com.climpy.redrock.redis.data.SubPubManager;
import com.climpy.redrock.server.ServerState;
import com.climpy.redrock.server.ServerType;
import com.climpy.redrock.utils.ConfigUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.IOException;
import java.util.logging.Level;

@Setter
@Getter
public class RedisConnection {
    private final RedrockPlugin redrockPlugin;

    private boolean setupMode = true;

    private DataPublisher dataPublisher;
    private SubPubManager dataManager;

    public RedisConnection(RedrockPlugin redrockPlugin) {
        this.redrockPlugin = redrockPlugin;
        redisConnection();
    }

    private void redisConnection() {
        try {
            Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[Redrock] [Redis]" + ChatColor.WHITE + " Veritabani ile baglantiya geciliyor..");
            setupRedisConnetion();
        } catch (IOException | JedisConnectionException iex) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[Redrock] [Redis]" + ChatColor.WHITE + " Yapilandirma dosyalari islenmiyor..");
            getShutDown();
        }
    }

    private void setupRedisConnetion() throws IOException, JedisConnectionException {
        ConfigUtils configRedis = new ConfigUtils(RedrockPlugin.getInstance().getMainConfig(), "redis");
        ConfigUtils configServer = new ConfigUtils(RedrockPlugin.getInstance().getMainConfig(), "server");
        String redisServer = configRedis.getString("server");
        int redisPort = configRedis.getInt("port");
        String redisPassword = configRedis.getString("password");
        String serverName = configServer.getString("name");
        int serverID = configServer.getInt("id");
        ServerType serverType = ServerType.getServerType(configServer.getString("type"));
        RedrockPlugin.getInstance().setServerName(serverName);
        RedrockPlugin.getInstance().setServerType(serverType);
        RedrockPlugin.getInstance().setServerState(ServerState.SETUP);
        RedrockPlugin.getInstance().setServerID(serverID);
        if (redisPassword != null && (redisPassword.isEmpty() || redisPassword.equals("none") || redisPassword
                .equals("yok") || redisPassword.equals("bos")))
            redisPassword = null;
        if (serverName == null || serverName.isEmpty()) {
            this.redrockPlugin.getLogger().log(Level.SEVERE, "Yapilandirma adi belirtilmedi.");
            (RedrockPlugin.getInstance()).jedisPool = null;
            Bukkit.getServer().shutdown();
            return;
        }
        if (redisServer != null && !redisServer.isEmpty()) {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(this.redrockPlugin.getMainConfig().getFileConfiguration().getInt("max-redis-connections"));
            (RedrockPlugin.getInstance()).jedisPool = new JedisPool((GenericObjectPoolConfig) jedisPoolConfig, redisServer, redisPort, 0, redisPassword);
            try (Jedis jedisPoolResource = RedrockPlugin.getInstance().getJedisPool().getResource()) {
                jedisPoolResource.ping();
                if (RedrockPlugin.getInstance().getJedisPool() != null) {
                    this.dataPublisher = new DataPublisher(RedrockPlugin.getInstance());
                    this.dataManager = new SubPubManager(RedrockPlugin.getInstance());
                    this.dataManager.createConnection();
                    Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[Redrock] [Redis]" + ChatColor.WHITE + " Veritabani sunucusuna basariyla baglandi.");
                    setSetupMode(false);
                    RedrockPlugin.getInstance().setServerState(ServerState.ACTIVE);
                } else {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[Redrock] [Redis]" + ChatColor.WHITE + " Veritabani sunucusuna baglanilmadi.");
                    getShutDown();
                }
            } catch (JedisConnectionException jce) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[Redrock] [Redis]" + ChatColor.WHITE + " Veritabani sunucusuna baglanilmadi.");
                this.redrockPlugin.getLogger().log(Level.SEVERE, jce.getMessage());
                getShutDown();
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[Redrock] [Redis]" + ChatColor.WHITE + " Veritabani suncusu belirtilmemis durumda.");
            getShutDown();
        }
    }

    private void getShutDown() {
        (RedrockPlugin.getInstance()).jedisPool = null;
        Bukkit.getServer().shutdown();
    }
}
