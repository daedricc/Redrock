package com.climpy.redis.messages;

import com.climpy.RedrockPlugin;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.user.User;
import com.climpy.redis.data.DataSubscriber;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BanAlertSubscriber extends DataSubscriber {
    public BanAlertSubscriber() {
      super(RedrockPlugin.getInstance(), "ban_alerts");
    }

    public void handleMessage(JsonObject object) {
      UUID uuid = UUID.fromString(object.get("uuid").getAsString());
      String reason = object.get("reason").getAsString();
      User onlineUser = ProfilePlugin.getInstance().getUserManager().getUser(uuid);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(ChatColor.RED.toString()).append(ChatColor.STRIKETHROUGH).append("------------------------------------------------");
      stringBuilder.append(ChatColor.RED).append("MCheat, ").append(ChatColor.YELLOW).append(onlineUser.getName()).append(ChatColor.RED).append(" adlı kişiyi ağdan kaldırdı.");
      stringBuilder.append(ChatColor.RED).append("Neden: ").append(ChatColor.YELLOW).append(reason);
      stringBuilder.append(ChatColor.RED.toString()).append(ChatColor.STRIKETHROUGH).append("------------------------------------------------");
      for (Player onlinePlayer : Bukkit.getOnlinePlayers())
        onlinePlayer.sendMessage(stringBuilder.toString());
    }
    }