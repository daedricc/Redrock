package com.climpy.listener;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.redis.RedisConnection;
import com.climpy.redis.RedisManager;
import com.climpy.redis.data.DataPublisher;
import com.climpy.redis.messages.StaffSubscriber;
import com.climpy.server.ServerData;
import com.climpy.server.provider.DefaultServerProvider;
import com.google.gson.JsonObject;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class RedrockListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(player.getName());

        DefaultServerProvider defaultServerProvider = new DefaultServerProvider();
        if (user.getRankType().isAboveOrEqual(RankType.CHAT_MOD)) {
            DataPublisher dataPublisher = RedrockPlugin.getInstance().getRedisConnection().getDataPublisher();
            JsonObject staffObject = new JsonObject();
            StaffSubscriber.StaffEventType staffEventType = StaffSubscriber.StaffEventType.JOIN;
            staffObject.addProperty("type", staffEventType.getInternalName());
            staffObject.addProperty("uuid", user.getUniqueUUID().toString());
            staffObject.addProperty("serverName", defaultServerProvider.getServerName());
            dataPublisher.write("staff_join_leave", staffObject);
        }
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());

        DefaultServerProvider defaultServerProvider = new DefaultServerProvider();
        if (user.getRankType().isAboveOrEqual(RankType.CHAT_MOD)) {
            DataPublisher dataPublisher = RedrockPlugin.getInstance().getRedisConnection().getDataPublisher();
            JsonObject staffObject = new JsonObject();
            StaffSubscriber.StaffEventType staffEventType = StaffSubscriber.StaffEventType.QUIT;
            staffObject.addProperty("type", staffEventType.getInternalName());
            staffObject.addProperty("uuid", user.getUniqueUUID().toString());
            staffObject.addProperty("serverName", defaultServerProvider.getServerName());
            dataPublisher.write("staff_join_leave", staffObject);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onAsyncPlayerJoinEvent(AsyncPlayerPreLoginEvent event) {
        RedisConnection redisConnection = RedrockPlugin.getInstance().getRedisConnection();
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED)
            return;
        if (redisConnection.isSetupMode()) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(ChatColor.RED + "Veritabanı kuruluyor");
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        RedisConnection redisConnection = RedrockPlugin.getInstance().getRedisConnection();
        Player eventPlayer = event.getPlayer();
        if (redisConnection.isSetupMode()) {
            Profile eventUser = RedrockPlugin.getInstance().getProfileManager().getProfile(eventPlayer.getUniqueId());
            if (eventUser.getRankType().isAboveOrEqual(RankType.MOD) && eventUser.isStaffChat())
                return;
            event.setCancelled(true);
            eventPlayer.sendMessage(ChatColor.RED + "Sunucu kuruluyor..");
            Location playerLocation = eventPlayer.getLocation();
            eventPlayer.playSound(playerLocation, Sound.CAT_HISS, 1.0F, 1.0F);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerKickEvent(PlayerKickEvent event) {
        Player eventPlayer = event.getPlayer();
        RedisManager redisManager = RedrockPlugin.getInstance().getRedisManager();
        DefaultServerProvider defaultServerProvider = new DefaultServerProvider();
        String serverName = defaultServerProvider.getServerName();
        ServerData serverData = redisManager.getSuitableLobby();
        if (serverData == null)
            return;
        Profile eventUser = RedrockPlugin.getInstance().getProfileManager().getProfile(eventPlayer.getUniqueId());
        if (!eventUser.getRankType().isAboveOrEqual(RankType.CHAT_MOD)) {
            event.setCancelled(true);
            String kickReason = ChatColor.RED + "Sunucudan atıldınız: " + ChatColor.LIGHT_PURPLE + serverName + ChatColor.RED + ": " + ChatColor.YELLOW + event.getReason();
            redisManager.kickRedisServer(eventUser.getUniqueUUID(), serverData, kickReason);

        }
    }
}
