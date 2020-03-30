package com.climpy.punishments.extnds;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.punishments.Punishment;
import com.climpy.punishments.PunishmentType;
import com.google.gson.JsonObject;
import org.bukkit.ChatColor;

import java.util.UUID;

public class Kick extends Punishment {

    public Kick(String ipAddress, UUID uniqueUUID, UUID addedBy, String addedReason) {
        super(PunishmentType.KICK, ipAddress, uniqueUUID, addedBy, addedReason);
    }

    public Kick(String ipAddress, UUID uniqueUUID, UUID addedBy, String addedReason, long duration) {
        super(PunishmentType.KICK, ipAddress, uniqueUUID, addedBy, addedReason, duration);
    }

    public Kick(JsonObject jsonObject) {
        super(jsonObject);
    }


    @Override
    public String getWarningMessage(boolean active, boolean silent) {
        StringBuilder message = new StringBuilder((silent ? "[Sessiz]" : ""));
        Profile staffProfile = RedrockPlugin.getInstance().getProfileManager().getProfile(active ? this.addedBy : this.removedBy);
        Profile targetProfile = RedrockPlugin.getInstance().getProfileManager().getProfile(this.uniqueUUID);

        if (active) {
            message.append(targetProfile.getActiveRank().getColor()).append(targetProfile.getName()).append(ChatColor.GREEN).append(" adlı oyuncu sunucudan atıldı, by ").
                    append(staffProfile.getActiveRank().getColor()).append(staffProfile.getName());
        }

        return message.toString();
    }

    @Override
    public StringBuilder getPunishmentMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ChatColor.RED).append("Sunucudan atıldın.").append("\n");
        stringBuilder.append(ChatColor.RED).append("Neden: ").append(ChatColor.WHITE).append(this.addedReason).append("\n");
        return stringBuilder;
    }
}
