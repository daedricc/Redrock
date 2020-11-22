package com.climpy.redrock;

import com.climpy.redrock.commands.handler.RedrockCommandHandler;
import com.climpy.redrock.config.FileConfig;
import com.climpy.redrock.inventory.InventoryManager;
import com.climpy.redrock.listener.ChannelListener;
import com.climpy.redrock.listener.RedrockListener;
import com.climpy.redrock.redis.RedisConnection;
import com.climpy.redrock.redis.RedisManager;
import com.climpy.redrock.redis.player.RedisPlayerCache;
import com.climpy.redrock.report.ReportManager;
import com.climpy.redrock.server.ServerState;
import com.climpy.redrock.server.ServerType;
import com.climpy.redrock.tasks.Lag;
import com.climpy.redrock.tasks.ServerDataTask;
import com.climpy.redrock.tasks.ServerRemoveTask;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.logging.Level;

@Getter
@Setter
public class RedrockPlugin extends JavaPlugin {
    private static RedrockPlugin instance;
    private FileConfig mainConfig;

    private static InventoryManager invManager;

    private boolean enableRedisPubSub = false;

    private String serverName;
    private ServerType serverType;
    private ServerState serverState;

    private int serverID;

    private RedisManager redisManager;
    private RedisConnection redisConnection;
    public JedisPubSub jedisPubSub;
    public JedisPool jedisPool;

    private final long startTime = System.currentTimeMillis();
    private long lastUpTime = System.currentTimeMillis();

    private RedisPlayerCache redisPlayerCache;

    private ReportManager reportManager;

    @Override
    public void onEnable() {
        instance = this;

        registerManagers();
        registerListeners();
        RedrockCommandHandler.init();

        if (!this.redisConnection.isSetupMode()) {
            getLogger().log(Level.INFO, "[Redis] - Sunucu: " + this.serverName + " - ID: " + serverID + " - Sunucu Tipi: " + this.getServerType().getDisplayName());
        }
    }

    @Override
    public void onDisable() {
        disableRedis();
        instance = null;
    }

    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new ChannelListener(), this);
        pm.registerEvents(new RedrockListener(), this);
    }

    private void disableRedis() {
        if (getJedisPool() != null) {
            Bukkit.getScheduler().cancelTasks((Plugin) this);
            getLogger().log(Level.INFO, "Veritabani kapatildi.");
            getRedisConnection().setSetupMode(true);
            getInstance().setServerState(ServerState.INACTIVE);
            (new ServerDataTask(this)).run();

            try {
                if (isEnableRedisPubSub())
                    this.jedisPubSub.unsubscribe();
                this.jedisPool.destroy();
                getLogger().log(Level.INFO, "Veritabani kapatildi.");
            } catch (Exception ex) {
                getLogger().log(Level.INFO, "Veritaban verileri kaydedilemedi.");
            }
        }
    }

    private void registerManagers() {
        this.mainConfig = new FileConfig(this, "config.yml");

        this.redisConnection = new RedisConnection(this);
        this.redisManager = new RedisManager(this);
        this.redisPlayerCache = new RedisPlayerCache();
        invManager = new InventoryManager(this);
        invManager.init();
        this.reportManager = new ReportManager();

        registerSchedulers();
    }

    private void registerSchedulers() {
        (new ServerDataTask(this)).runTaskTimerAsynchronously(this, 50L, 50L);
        (new ServerRemoveTask(this)).runTaskTimerAsynchronously(this, 20L, 20L);
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Lag(), 100L, 1L);
    }

    public static RedrockPlugin getInstance() {
        return instance;
    }

    public static InventoryManager manager() {
        return invManager;
    }
}
