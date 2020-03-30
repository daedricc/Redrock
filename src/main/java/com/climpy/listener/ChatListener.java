package com.climpy.listener;

import com.climpy.RedrockPlugin;
import com.climpy.manager.ChatManager;
import com.climpy.profile.Profile;
import com.climpy.punishments.Punishment;
import com.climpy.punishments.PunishmentManager;
import com.climpy.punishments.PunishmentType;
import com.climpy.redis.data.DataPublisher;
import com.climpy.server.provider.DefaultServerProvider;
import com.climpy.utils.C;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void AsyncChatEvent(AsyncPlayerChatEvent event) {
        Profile profile = RedrockPlugin.getInstance().getProfileManager().getProfile(event.getPlayer().getUniqueId());
        String message = event.getMessage();

        PunishmentManager punishmentManager = RedrockPlugin.getInstance().getPunishmentManager();
        Punishment punishment = punishmentManager.getPunishment(profile.getUniqueUUID(), PunishmentType.IP_MUTE, PunishmentType.MUTE);
        if (punishment != null) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(punishment.getPunishmentMessage().toString());
        }

        if (!profile.toPlayer().hasPermission("redrock.mod") && profile.isStaffChat()) {
            event.setCancelled(true);
            if (profile.isStaffChat()) {
                DefaultServerProvider defaultServerProvider = new DefaultServerProvider();
                DataPublisher dataPublisher = RedrockPlugin.getInstance().getRedisConnection().getDataPublisher();
                JsonObject chatObject = new JsonObject();
                chatObject.addProperty("uuid", profile.getUniqueUUID().toString());
                chatObject.addProperty("serverName", defaultServerProvider.getServerName());
                chatObject.addProperty("message", message);
                dataPublisher.write("staff_chat", chatObject);
                return;
            }

            if (!profile.toPlayer().hasPermission("redrock.mod")) {
                profile.setStaffChat(false);
                profile.toPlayer().sendMessage(C.RED + "Yetkiniz olmadığı için personel sohbetiniz kapatıldı.");
                return;
            }

            ChatManager chatManager = RedrockPlugin.getInstance().getChatManager();
            if (ChatManager.BLOCKED_WORDS.contains(message.toLowerCase())) {
                event.setCancelled(true);
                profile.toPlayer().playSound(profile.toPlayer().getLocation(), Sound.DONKEY_HIT, 1.0F, 1.0F);
                profile.toPlayer().sendMessage(C.translate("&4[⚠] &cYazılan kelime yasaklanmış, yetkililere bildirildi."));

                for (Player playerStaff : Bukkit.getOnlinePlayers()) {
                    Profile staffUser = RedrockPlugin.getInstance().getProfileManager().getProfile(playerStaff.getUniqueId());
                    if (staffUser.toPlayer().hasPermission("redrock.mod"))
                        playerStaff.sendMessage(C.translate("&4[⚠] &c(Sansürlendi) " + profile.getActiveRank().getPrefix() + profile.getName() + ChatColor.DARK_GRAY + ": " + ChatColor.RESET + message));
                }
            }

        }
    }
}