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

public class RequestSubscriber extends DataSubscriber {
    public RequestSubscriber() {
        super(RedrockPlugin.getInstance(), "request_alerts");
    }

    public void handleMessage(JsonObject object) {
        UUID uuid = UUID.fromString(object.get("uuid").getAsString());
        String serverName = object.get("serverName").getAsString();
        String message = object.get("message").getAsString();
        Profile requestedUser = TorchPlugin.getInstance().getProfileManager().getProfile(uuid);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Profile onlineUser = TorchPlugin.getInstance().getProfileManager().getProfile(onlinePlayer.getUniqueId());
            if (onlineUser.toPlayer().hasPermission("redrock.moderator")) {
                String stringBuilder = ChatColor.DARK_GREEN + "[İstek] " + ChatColor.WHITE + "[" + serverName + "] " + ChatColor.GREEN + requestedUser.getName() + ChatColor.GRAY + " yeni bir istek gönderdi.\n" + ChatColor.DARK_GREEN + "     Neden: " + ChatColor.GRAY + message;
                onlinePlayer.sendMessage(stringBuilder);
            }
        }
    }
}
