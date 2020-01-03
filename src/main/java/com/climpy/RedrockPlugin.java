package com.climpy;

import com.climpy.commands.commands.*;
import com.climpy.commands.network.*;
import com.climpy.commands.network.players.ReportCommand;
import com.climpy.commands.network.players.RequestCommand;
import com.climpy.config.FileConfig;
import com.climpy.inv.InventoryManager;
import com.climpy.listener.ChannelListener;
import com.climpy.listener.PlayerJoinListener;
import com.climpy.listener.RedrockListener;
import com.climpy.profile.utils.TimeUtils;
import com.climpy.punishments.PunishmentManager;
import com.climpy.punishments.args.*;
import com.climpy.redis.RedisConnection;
import com.climpy.redis.RedisManager;
import com.climpy.server.ServerData;
import com.climpy.server.ServerState;
import com.climpy.server.ServerType;
import com.climpy.server.commands.ServerChangeCommand;
import com.climpy.server.commands.ServerUpdateCommand;
import com.climpy.tasks.Lag;
import com.climpy.tasks.ServerDataTask;
import com.climpy.tasks.ServerRemoveTask;
import com.climpy.utils.TimeUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.logging.Level;

@Getter @Setter
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
    private PunishmentManager punishmentManager;

    @Override
    public void onEnable() {
        instance = this;
        getServer().getMessenger().registerOutgoingPluginChannel((Plugin)this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel((Plugin)this, "BungeeCord", (PluginMessageListener)new ChannelListener());



        invManager = new InventoryManager(this);
        invManager.init();

        registerManagers();
        registerListeners();
        registerCommands();

        if (!this.redisConnection.isSetupMode()) {
            getLogger().log(Level.INFO, "===============================================");
            getLogger().log(Level.INFO, "Sunucu ismi: " + this.serverName);
            getLogger().log(Level.INFO, "Veritabankimli" + this.serverID);
            getLogger().log(Level.INFO, "Sunucu t" + this.serverType.getDisplayName());
            getLogger().log(Level.INFO, "===============================================");
        }
    }

    @Override
    public void onDisable() {
        if (getJedisPool() != null) {
            Bukkit.getScheduler().cancelTasks((Plugin)this);
            getLogger().log(Level.INFO, "Veritabanı kapatıldı.");
                    getRedisConnection().setSetupMode(true);
            getInstance().setServerState(ServerState.INACTIVE);
            (new ServerDataTask(this)).run();
            try {
                if (isEnableRedisPubSub())
                    this.jedisPubSub.unsubscribe();
                this.jedisPool.destroy();
                getLogger().log(Level.INFO, "Veritabanı kapatıldı.");
            } catch (Exception ex) {
                getLogger().log(Level.INFO, "Veritabanverileri kaydedilemedi.");
            }
        }
        instance = null;
    }


    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents((Listener)new ChannelListener(), (Plugin)this);
        pm.registerEvents(new PlayerJoinListener(), this);
        pm.registerEvents(new RedrockListener(), this);
    }

    private void registerManagers() {
        this.mainConfig = new FileConfig(this, "config.yml");

        this.mainConfig = new FileConfig(this, "config.yml");
        this.redisConnection = new RedisConnection(this);
        this.redisManager = new RedisManager(this);
        this.punishmentManager = new PunishmentManager(this);
        registerSchedulers();
    }

    private void registerCommands() {
        getCommand("broadcast").setExecutor(new BroadcastCommand());
        getCommand("day").setExecutor(new DayCommand());
        getCommand("info").setExecutor(new InfoCommand());
        getCommand("night").setExecutor(new NightCommand());
        getCommand("createworld").setExecutor(new WorldCreateCommand());
        getCommand("world").setExecutor(new WorldTeleportCommand());
        getCommand("gamemode").setExecutor(new GameModeCommand());
        getCommand("chatclear").setExecutor(new ChatClearCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("setspawn").setExecutor(new SetSpawnCommand());
        getCommand("clear").setExecutor(new ClearCommand());
        getCommand("setslots").setExecutor(new SetSlotsCommand());
        getCommand("hat").setExecutor(new HatCommand());
        getCommand("more").setExecutor(new MoreCommand());
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("ping").setExecutor(new PingCommand());
        getCommand("teleport").setExecutor(new TeleportCommand());
        getCommand("teleportall").setExecutor(new TeleportCommand());
        getCommand("tphere").setExecutor(new TeleportCommand());
        getCommand("tppos").setExecutor(new TeleportCommand());

        getCommand("alert").setExecutor(new AlertCommand());
        getCommand("server").setExecutor(new ServerCommand());
        getCommand("report").setExecutor(new ReportCommand());
        getCommand("request").setExecutor(new RequestCommand());
        getCommand("lobby").setExecutor(new LobbyCommand());
        getCommand("status").setExecutor(new StatusCommand());
        getCommand("glist").setExecutor(new NetworkPlayersCommand());
        getCommand("serverchange").setExecutor(new ServerChangeCommand());
        getCommand("serverupdate").setExecutor(new ServerUpdateCommand());
        getCommand("speed").setExecutor(new SpeedCommand());
        getCommand("sudo").setExecutor(new SudoCommand());
        getCommand("find").setExecutor(new FindCommand());
        getCommand("whitelist").setExecutor(new WhitelistCommand());

       getCommand("ban").setExecutor(new BanCommand());
       getCommand("blacklist").setExecutor(new BlacklistCommand());
       getCommand("kick").setExecutor(new KickCommand());
       getCommand("mute").setExecutor(new MuteCommand());
       getCommand("unban").setExecutor(new UnBanCommand());
       getCommand("unblacklist").setExecutor(new UnBlacklistCommand());
       getCommand("unmute").setExecutor(new UnMuteCommand());
    }

    private void registerSchedulers() {
        (new ServerDataTask(this)).runTaskTimerAsynchronously((Plugin)this, 50L, 50L);
        (new ServerRemoveTask(this)).runTaskTimerAsynchronously((Plugin)this, 20L, 20L);
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Lag(), 100L, 1L);
    }

    public static RedrockPlugin getInstance() { return instance; }
    public static InventoryManager manager() { return invManager; }

    public static ChatColor tpsColor(double spigotTPS) {
        ChatColor chatColorType;
        if (spigotTPS >= 18.0D) {
            chatColorType = ChatColor.DARK_GREEN;
        } else if (spigotTPS >= 15.0D) {
            chatColorType = ChatColor.YELLOW;
        } else {
            chatColorType = ChatColor.RED;
        }
        return chatColorType;
    }
}
