package com.climpy.redrock;

import com.climpy.redrock.commands.RedrockRegisterCommand;
import com.climpy.redrock.config.FileConfig;
import com.climpy.redrock.inventory.InventoryManager;
import com.climpy.redrock.listener.ChannelListener;
import com.climpy.redrock.redis.RedisConnection;
import com.climpy.redrock.redis.RedisManager;
import com.climpy.redrock.redis.player.RedisPlayerCache;
import com.climpy.redrock.server.ServerState;
import com.climpy.redrock.server.ServerType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

@Getter @Setter
public class RedrockPlugin extends JavaPlugin {
    public static RedrockPlugin instance;

    private FileConfig mainConfig;
    private RedrockRegisterCommand redrockRegisterCommand;

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

    private static InventoryManager invManager;

    @Override
    public void onEnable() {
        instance = this;

        RedrockPlugin.getInstance().getServer().getMessenger().registerOutgoingPluginChannel((Plugin) RedrockPlugin.getInstance(), "BungeeCord");
        RedrockPlugin.getInstance().getServer().getMessenger().registerIncomingPluginChannel((Plugin) RedrockPlugin.getInstance(), "BungeeCord", new ChannelListener());

        registerManagers();
        registerListeners();
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    private void registerManagers() {
        this.mainConfig = new FileConfig(this, "config.yml");

        this.redisConnection = new RedisConnection(this);
        this.redisManager = new RedisManager(this);
        this.redisPlayerCache = new RedisPlayerCache();

        invManager = new InventoryManager(this);
        invManager.init();

        this.redrockRegisterCommand = new RedrockRegisterCommand();
    }

    private void registerListeners() {
        RedrockPlugin.getInstance().getServer().getPluginManager().registerEvents(new ChannelListener(), RedrockPlugin.getInstance());
    }

    public static RedrockPlugin getInstance() { return instance; }
    public static InventoryManager manager() { return invManager; }
}
