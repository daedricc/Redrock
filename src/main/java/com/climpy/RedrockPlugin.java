package com.climpy;

import com.climpy.autoreboot.AutoRebootHandler;
import com.climpy.command.handler.RedrockCommandHandler;
import com.climpy.config.FileConfig;
import com.climpy.deathmessage.RedrockDeathMessageHandler;
import com.climpy.inv.InventoryManager;
import com.climpy.listener.ChannelListener;
import com.climpy.listener.ChatListener;
import com.climpy.listener.CommandListener;
import com.climpy.listener.RedrockListener;
import com.climpy.manager.ChatManager;
import com.climpy.mongo.MongoConnection;
import com.climpy.profile.Profile;
import com.climpy.profile.ProfileManager;
import com.climpy.profile.grant.listener.GrantListener;
import com.climpy.profile.moderator.FollowManager;
import com.climpy.profile.moderator.StaffManager;
import com.climpy.profile.moderator.VanishManager;
import com.climpy.profile.listener.ProfileListener;
import com.climpy.profile.moderator.listener.FollowListener;
import com.climpy.profile.moderator.listener.StaffListener;
import com.climpy.profile.moderator.listener.VanishListener;
import com.climpy.profile.rank.Rank;
import com.climpy.profile.redis.RedisPlayerCache;
import com.climpy.punishments.PunishmentListener;
import com.climpy.punishments.PunishmentManager;
import com.climpy.redis.RedisConnection;
import com.climpy.redis.RedisManager;
import com.climpy.report.ReportManager;
import com.climpy.server.ServerState;
import com.climpy.server.ServerType;
import com.climpy.tasks.Lag;
import com.climpy.tasks.ServerDataTask;
import com.climpy.tasks.ServerRemoveTask;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
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

    public ProfileManager profileManager;

    private MongoConnection mongoConnection;

    private RedrockCommandHandler redrockCommandHandler;

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

    private StaffManager staffManager;
    private VanishManager vanishManager;

    private ChatManager chatManager;
    private FollowManager followManager;

    @Override
    public void onEnable() {
        instance = this;

        ChannelListener.init();

        registerManagers();
        registerListeners();
        this.redrockCommandHandler.init();

        if (!this.redisConnection.isSetupMode()) {
            getLogger().log(Level.INFO, "[Redis] - Sunucu: " + this.serverName + " - ID: " + serverID + " - Sunucu Tipi: " + this.getServerType().getDisplayName());
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Profile profile = profileManager.getProfile(onlinePlayer.getUniqueId());
            if (profile == null) {
                continue;
            }

            if (profile.toPlayer().hasPermission("redrock.mod")) {
                if (profile.isStaffMode() && !this.staffManager.getStaffs().contains(profile.getUniqueUUID())) {
                    this.staffManager.setStaffMode(profile.getUniqueUUID(), true);
                }

                if (profile.toPlayer().hasMetadata("vanish")) {
                    profile.toPlayer().setMetadata("vanish", new FixedMetadataValue(RedrockPlugin.getInstance(), true));
                }
            }
        }
    }

    @Override
    public void onDisable() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer == null) {
                continue;
            }

            Profile profile = profileManager.getProfile(onlinePlayer.getUniqueId());
            if (profile == null) {
                continue;
            }

            if (profile.toPlayer().hasPermission("redrock.mod") && profile.isStaffMode()) {
                this.staffManager.setStaffMode(profile.getUniqueUUID(), false);
            }
        }

        disableRedis();
        instance = null;
    }

    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new ChannelListener(), this);
        pm.registerEvents(new ProfileListener(), this);
        pm.registerEvents(new GrantListener(this), this);
        pm.registerEvents(new RedrockListener(), this);
        pm.registerEvents(new PunishmentListener(), this);
        pm.registerEvents(new ChatListener(), this);

        pm.registerEvents(new StaffListener(), this);
        pm.registerEvents(new VanishListener(), this);
        pm.registerEvents(new FollowListener(), this);
        pm.registerEvents(new CommandListener(), this);
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

        mongoConnection = new MongoConnection();
        mongoConnection.setupDatabaseConnection();
        Rank.init();
        profileManager = new ProfileManager();
        this.redisConnection = new RedisConnection(this);
        this.redisManager = new RedisManager(this);
        this.redisPlayerCache = new RedisPlayerCache();
        invManager = new InventoryManager(this);
        invManager.init();
        this.reportManager = new ReportManager();
        this.punishmentManager = new PunishmentManager();
        this.staffManager = new StaffManager();
        this.vanishManager = new VanishManager();
        this.chatManager = new ChatManager(this);
        this.followManager = new FollowManager();
        this.redrockCommandHandler = new RedrockCommandHandler();

        AutoRebootHandler.init();
        RedrockDeathMessageHandler.init();

        registerSchedulers();
    }

    private void registerSchedulers() {
        (new ServerDataTask(this)).runTaskTimerAsynchronously(this, 50L, 50L);
        (new ServerRemoveTask(this)).runTaskTimerAsynchronously(this, 20L, 20L);
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Lag(), 100L, 1L);
        Profile.runnable();
    }

    public static RedrockPlugin getInstance() {
        return instance;
    }

    public static InventoryManager manager() {
        return invManager;
    }
}
