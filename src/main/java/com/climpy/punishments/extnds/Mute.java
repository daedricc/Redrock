package com.climpy.punishments.extnds;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.punishments.Punishment;
import com.climpy.punishments.PunishmentType;
import com.climpy.utils.TimeUtils;
import com.google.gson.JsonObject;
import org.bukkit.ChatColor;

import java.util.UUID;

public class Mute extends Punishment {

    public Mute(PunishmentType punishmentType, String ipAddress, UUID uniqueUUID, UUID addedBy, String addedReason) {
        super(punishmentType, ipAddress, uniqueUUID, addedBy, addedReason);
    }

    public Mute(PunishmentType punishmentType, String ipAddress, UUID uniqueUUID, UUID addedBy, String addedReason, long duration) {
        super(punishmentType, ipAddress, uniqueUUID, addedBy, addedReason, duration);
    }

    public Mute(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public String getWarningMessage(boolean active, boolean silent) {
        StringBuilder message = new StringBuilder((silent ? "[Sessiz]" : ""));
        Profile staffProfile = RedrockPlugin.getInstance().getProfileManager().getProfile(active ? this.addedBy : this.removedBy);
        Profile targetProfile = RedrockPlugin.getInstance().getProfileManager().getProfile(this.uniqueUUID);

        if (active) {
            message.append(targetProfile.getRankType().getColor()).append(targetProfile.getName()).append(ChatColor.GREEN).append(" adlı oyuncu sunucuda susturuldu, by ").
                    append(staffProfile.getRankType().getColor()).append(staffProfile.getName());
        } else {
            message.append(targetProfile.getRankType().getColor()).append(targetProfile.getName()).append(ChatColor.GREEN).append(" adlı oyuncunun susturulması açıldı, by ")
                    .append(staffProfile.getRankType().getColor()).append(staffProfile.getName());
        }

        return message.toString();
    }

    @Override
    public StringBuilder getPunishmentMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n").append("\n");
        stringBuilder.append(ChatColor.RED.toString()).append(ChatColor.BOLD).append(this.punishmentType == PunishmentType.IP_MUTE ? "IP " : "").append("Susturuldun").append(ChatColor.RED).append("! ").append(this.isPermanent() ? "Bu cezanın süresi yoktur." : "Süre aşağıda yer almaktadır.\n" + ChatColor.RED + "Kalan süre: " + ChatColor.YELLOW.toString() + ChatColor.BOLD + TimeUtils.getDurationWords(this.duration - System.currentTimeMillis(), true, true) + ChatColor.RED + ".").append("\n");
        stringBuilder.append(ChatColor.RED).append("Neden: ").append(ChatColor.WHITE).append(this.addedReason).append("\n");
        stringBuilder.append(ChatColor.RED).append("Geri bildirim için, ").append(ChatColor.BOLD).append("moosy.net").append(ChatColor.RED).append(".");
        stringBuilder.append("\n").append("\n ");
        return stringBuilder;
    }


}
