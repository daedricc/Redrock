package com.climpy.punishments.extnds;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.punishments.Punishment;
import com.climpy.punishments.PunishmentType;
import com.google.gson.JsonObject;
import org.bukkit.ChatColor;

import java.util.UUID;

public class Blacklist extends Punishment {

    public Blacklist(String ipAddress, UUID uniqueUUID, UUID addedBy, String addedReason) {
        super(PunishmentType.BLACKLIST, ipAddress, uniqueUUID, addedBy, addedReason);
    }

    public Blacklist(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public String getWarningMessage(boolean active, boolean silent) {
        StringBuilder message = new StringBuilder((silent ? "[Sessiz]" : ""));
        Profile staffProfile = RedrockPlugin.getInstance().getProfileManager().getProfile(active ? this.addedBy : this.removedBy);
        Profile targetProfile = RedrockPlugin.getInstance().getProfileManager().getProfile(this.uniqueUUID);

        if (active) {
            message.append(targetProfile.getActiveRank().getColor()).append(targetProfile.getName()).append(ChatColor.GREEN).append(" adlı oyuncu karalisteye alındı, by ").
                    append(staffProfile.getActiveRank().getColor()).append(staffProfile.getName());
        } else {
            message.append(targetProfile.getActiveRank().getColor()).append(targetProfile.getName()).append(ChatColor.GREEN).append(" adlı oyuncu karalisteden kaldırıldı, by ")
                    .append(staffProfile.getActiveRank().getColor()).append(staffProfile.getName());
        }

        return message.toString();
    }

    @Override
    public StringBuilder getPunishmentMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ChatColor.RED).append("Hesabınız ağ üzerinde karalisteye alındı.").append("\n").append("\n");
        stringBuilder.append(ChatColor.RED).append("Bu tür cezalarda geri bildirim kabul edilemez.").append("\n");
        return stringBuilder;
    }
}
