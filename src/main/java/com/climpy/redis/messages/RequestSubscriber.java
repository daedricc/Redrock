package com.elice.bedrock.redis.messages.extnds;

import com.climpy.RedrockPlugin;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.redis.data.DataSubscriber;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RequestSubscriber extends DataSubscriber {
    public RequestSubscriber() {
        super(RedrockPlugin.getInstance(), "request_alerts");
    }

    public void handleMessage(JsonObject object) {
        UUID uuid = UUID.fromString(object.get("uuid").getAsString());
        String serverName = object.get("serverName").getAsString();
        String message = object.get("message").getAsString();
        User requestedUser = ProfilePlugin.getInstance().getUserManager().getUser(uuid);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            User onlineUser = ProfilePlugin.getInstance().getUserManager().getUser(onlinePlayer.getUniqueId());
            if (onlineUser.getRankType().isAboveOrEqual(RankType.CHAT_MOD)) {
                String stringBuilder = ChatColor.DARK_GREEN + "[İstek] " + ChatColor.WHITE + "[" + serverName + "] " + ChatColor.GREEN + requestedUser.getName() + ChatColor.GRAY + " yeni bir istek gönderdi.\n" + ChatColor.DARK_GREEN + "     Neden: " + ChatColor.GRAY + message;
                onlinePlayer.sendMessage(stringBuilder);
            }
        }
    }
}
