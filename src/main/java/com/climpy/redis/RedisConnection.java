package com.climpy.redis;

import com.climpy.RedrockPlugin;
import com.climpy.redis.data.DataPublisher;
import com.climpy.redis.data.SubPubManager;
import com.climpy.server.ServerState;
import com.climpy.server.ServerType;
import com.climpy.utils.ConfigUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.IOException;
import java.util.logging.Level;

@Setter @Getter
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
        this.redrockPlugin.getLogger().log(Level.INFO, "Veritabanı ile bağlantıya geçiliyor..");
        setupRedisConnetion();
      } catch (IOException iex) {
        this.redrockPlugin.getLogger().log(Level.SEVERE, "Yapılandırma dosyaları işlenmiyor..");
        getShutDown();
      } catch (JedisConnectionException jce) {
        this.redrockPlugin.getLogger().log(Level.SEVERE, "Redise bağlanılmıyor.");
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
        .equals("yok") || redisPassword.equals("boş")))
        redisPassword = null;
      if (serverName == null || serverName.isEmpty()) {
        this.redrockPlugin.getLogger().log(Level.SEVERE, "Yapılandırma adı belirtilmedi.");
        (RedrockPlugin.getInstance()).jedisPool = null;
        Bukkit.getServer().shutdown();
        return;
      }
      if (redisServer != null && !redisServer.isEmpty()) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(this.redrockPlugin.getMainConfig().getFileConfiguration().getInt("max-redis-connections"));
        (RedrockPlugin.getInstance()).jedisPool = new JedisPool((GenericObjectPoolConfig)jedisPoolConfig, redisServer, redisPort, 0, redisPassword);
        try (Jedis jedisPoolResource = RedrockPlugin.getInstance().getJedisPool().getResource()) {
          jedisPoolResource.ping();
          if (RedrockPlugin.getInstance().getJedisPool() != null) {
            this.dataPublisher = new DataPublisher(RedrockPlugin.getInstance());
            this.dataManager = new SubPubManager(RedrockPlugin.getInstance());
            this.dataManager.createConnection();
            this.redrockPlugin.getLogger().log(Level.INFO, "Veritabanı sunucusuna başarıyla bağlandı.");
            setSetupMode(false);
            RedrockPlugin.getInstance().setServerState(ServerState.ACTIVE);
          } else {
            this.redrockPlugin.getLogger().log(Level.SEVERE, "Veritabanı sunucusuna bağlanılmadı.");
            getShutDown();
          }
        } catch (JedisConnectionException jce) {
          this.redrockPlugin.getLogger().log(Level.SEVERE, "Veritabanı sunucusuna bağlanılmadı.");
          this.redrockPlugin.getLogger().log(Level.SEVERE, jce.getMessage());
          getShutDown();
        }
      } else {
        this.redrockPlugin.getLogger().log(Level.SEVERE, "Veritabanı suncusu belirtilmemiş durumda.");
        getShutDown();
      }
    }

    private void getShutDown() {
      (RedrockPlugin.getInstance()).jedisPool = null;
      Bukkit.getServer().shutdown();
    }
  }
