package com.climpy.redrock.redis.messages;

import com.climpy.redrock.RedrockPlugin;
import com.climpy.redrock.redis.data.DataSubscriber;
import com.climpy.torch.TorchPlugin;
import com.climpy.torch.profile.Profile;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ReportSubscriber extends DataSubscriber {
    public ReportSubscriber() {
        super(RedrockPlugin.getInstance(), "report_alerts");
    }

    public void handleMessage(JsonObject object) {
        UUID reportedUUID = UUID.fromString(object.get("reportedUUID").getAsString());
        UUID reporterUUID = UUID.fromString(object.get("reporterUUID").getAsString());
        String serverName = object.get("serverName").getAsString();
        String message = object.get("message").getAsString();
        Profile reportedUser = TorchPlugin.getInstance().getProfileManager().getProfile(reportedUUID);
        Profile reporterUser = TorchPlugin.getInstance().getProfileManager().getProfile(reporterUUID);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Profile onlineUser = TorchPlugin.getInstance().getProfileManager().getProfile(onlinePlayer.getUniqueId());
            if (onlineUser.getRank().hasPermission("torch.staff")) {
                String stringBuilder = ChatColor.GOLD + "[Rapor] " + ChatColor.WHITE + "[" + serverName + "] " + ChatColor.YELLOW + reportedUser.getName() + ChatColor.GRAY + ", " + ChatColor.YELLOW + reporterUser.getName() + ChatColor.GRAY + " tarafından raporlandı.\n" + ChatColor.GOLD + "     Neden: " + ChatColor.GRAY + message;
                onlinePlayer.sendMessage(stringBuilder);
            }
        }
    }
}
