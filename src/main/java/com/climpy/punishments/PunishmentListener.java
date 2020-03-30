package com.climpy.punishments;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.ProfileManager;
import com.climpy.punishments.event.PunishmentApplyEvent;
import com.climpy.punishments.event.PunishmentExpireEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.List;

public class PunishmentListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPunishmentApplyEvent(PunishmentApplyEvent event) {
        List<Punishment> punishments = event.getAltPunishments();
        Punishment punishment = event.getPunishment();

        boolean announce = event.isAnnounce();
        boolean silent = event.isSilent();

        ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();

        if (announce) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                Profile profile = profileManager.getProfile(onlinePlayer.getUniqueId());
                if (silent && !profile.toPlayer().hasPermission("redrock.mod")) {
                    continue;
                }

                onlinePlayer.sendMessage(punishment.getWarningMessage(punishment.isActive(), silent));
            }
        }

        for (Punishment altPunishment : punishments) {
            Player targetPlayer = Bukkit.getPlayer(altPunishment.getUniqueUUID());

            if (altPunishment.isActive() && targetPlayer != null) {
                if (altPunishment.getPunishmentType() == PunishmentType.MUTE || altPunishment.getPunishmentType() == PunishmentType.IP_MUTE || altPunishment.getPunishmentType() == PunishmentType.WARN) {
                    targetPlayer.sendMessage(altPunishment.getPunishmentMessage().toString());
                } else {
                    Bukkit.getScheduler().runTask(RedrockPlugin.getInstance(), () -> {
                        targetPlayer.kickPlayer(altPunishment.getPunishmentMessage().toString());
                    });
                }
            }

            Profile profile = profileManager.getProfile(altPunishment.getUniqueUUID());
            profile.getPunishments().removeIf(other -> other.getPunishmentID().equals(altPunishment.getPunishmentID()));
            profile.getPunishments().add(altPunishment);

            profile.saveProfile();
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPunishmentExpireEvent(PunishmentExpireEvent event) {
        Punishment punishment = event.getPunishment();

        punishment.setRemovedReason("Ceza süre aşımı.");
        punishment.setRemovedBy(null);
        punishment.setActive(false);

        PunishmentManager punishmentManager = RedrockPlugin.getInstance().getPunishmentManager();
        punishmentManager.applyPunishment(punishment, true, false);
    }

}
