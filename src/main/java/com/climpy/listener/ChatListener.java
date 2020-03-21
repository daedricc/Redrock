package com.climpy.listener;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.ProfileManager;
import com.climpy.punishments.Punishment;
import com.climpy.punishments.PunishmentManager;
import com.climpy.punishments.PunishmentType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void AsyncChatEvent(AsyncPlayerChatEvent event) {
        Profile profile = RedrockPlugin.getInstance().getProfileManager().getProfile(event.getPlayer().getUniqueId());

        PunishmentManager punishmentManager = RedrockPlugin.getInstance().getPunishmentManager();
        Punishment punishment = punishmentManager.getPunishment(profile.getUniqueUUID(), PunishmentType.IP_MUTE, PunishmentType.MUTE);
        if (punishment != null) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(punishment.getPunishmentMessage().toString());
        }
    }
}
