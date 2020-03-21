package com.climpy;

import com.climpy.core.commands.*;
import com.climpy.core.network.*;
import com.climpy.core.network.players.ReportCommand;
import com.climpy.core.network.players.RequestCommand;
import com.climpy.config.FileConfig;
import com.climpy.inv.InventoryManager;
import com.climpy.listener.ChannelListener;
import com.climpy.listener.ChatListener;
import com.climpy.listener.PlayerJoinListener;
import com.climpy.profile.commands.ProfileCommand;
import com.climpy.profile.commands.StaffChatCommand;
import com.climpy.profile.commands.StaffModeCommand;
import com.climpy.profile.listener.ProfileListener;
import com.climpy.listener.RedrockListener;
import com.climpy.mongo.MongoConnection;
import com.climpy.profile.ProfileManager;
import com.climpy.profile.rank.commands.ListRankCommand;
import com.climpy.profile.rank.commands.SetRankCommand;
import com.climpy.profile.redis.RedisPlayerCache;
import com.climpy.profile.symbol.commands.SetSymbolCommand;
import com.climpy.profile.symbol.commands.SymbolCommand;
import com.climpy.punishments.Punishment;
import com.climpy.punishments.PunishmentListener;
import com.climpy.punishments.PunishmentManager;
import com.climpy.punishments.args.*;
import com.climpy.redis.RedisConnection;
import com.climpy.redis.RedisManager;
import com.climpy.report.ReportManager;
import com.climpy.server.ServerState;
import com.climpy.server.ServerType;
import com.climpy.server.commands.ServerChangeCommand;
import com.climpy.server.commands.ServerUpdateCommand;
import com.climpy.tasks.Lag;
import com.climpy.tasks.ServerDataTask;
import com.climpy.tasks.ServerRemoveTask;
import com.climpy.utils.menu.MenuListener;
import com.google.gson.reflect.TypeToken;
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

import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Level;

@Getter @Setter
public class RedrockPlugin extends JavaPlugin {
    private static RedrockPlugin instance;
    private FileConfig mainConfig;

    private static InventoryManager invManager;

    public ProfileManager profileManager;

    private MongoConnection mongoConnection;

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
    private PunishmentManager punishmentManager;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getMessenger().registerOutgoingPluginChannel((Plugin)this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel((Plugin)this, "BungeeCord", (PluginMessageListener)new ChannelListener());

        mongoConnection = new MongoConnection();
        mongoConnection.setupDatabaseConnection();

        profileManager = new ProfileManager();

        invManager = new InventoryManager(this);
        invManager.init();

        registerManagers();
        registerListeners();
        registerCommands();

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
        pm.registerEvents(new ProfileListener(), this);
        pm.registerEvents(new PlayerJoinListener(), this);
        pm.registerEvents(new RedrockListener(), this);
        pm.registerEvents(new MenuListener(), this);
        pm.registerEvents(new PunishmentListener(), this);
        pm.registerEvents(new ChatListener(), this);
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
        getCommand("coins").setExecutor(new CoinsCommand());

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

        getCommand("staffchat").setExecutor(new StaffChatCommand());
        getCommand("staffmode").setExecutor(new StaffModeCommand());

        getCommand("rank").setExecutor(new SetRankCommand());
        getCommand("listrank").setExecutor(new ListRankCommand());

        getCommand("profile").setExecutor(new ProfileCommand());

        getCommand("symbol").setExecutor(new SymbolCommand());
        getCommand("setsymbol").setExecutor(new SetSymbolCommand());
        getCommand("whois").setExecutor(new WhoisCommand());

        getCommand("ban").setExecutor(new BanCommand());
        getCommand("blacklist").setExecutor(new BlacklistCommand());
        getCommand("kick").setExecutor(new KickCommand());
        getCommand("mute").setExecutor(new MuteCommand());
        getCommand("unban").setExecutor(new UnBanCommand());
        getCommand("unblacklist").setExecutor(new UnBlacklistCommand());
        getCommand("unmute").setExecutor(new UnMuteCommand());
        getCommand("warn").setExecutor(new WarnCommand());
        getCommand("ipmute").setExecutor(new IPMuteCommand());
        getCommand("ipban").setExecutor(new IPBanCommand());
    }

    private void disableRedis() {
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
                getLogger().log(Level.INFO, "Veritaban verileri kaydedilemedi.");
            }
        }
    }

    private void registerManagers() {
        this.mainConfig = new FileConfig(this, "config.yml");

        this.redisConnection = new RedisConnection(this);
        this.redisManager = new RedisManager(this);
        this.redisPlayerCache = new RedisPlayerCache();
        this.reportManager = new ReportManager();
        this.punishmentManager = new PunishmentManager();

        registerSchedulers();
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

    public static Type LIST_STRING_TYPE = (new TypeToken<List<String>>() {

    }).getType();
}
