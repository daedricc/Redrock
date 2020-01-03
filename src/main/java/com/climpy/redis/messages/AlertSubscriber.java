package com.climpy.redis.messages;

import com.climpy.RedrockPlugin;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.user.User;
import com.climpy.redis.data.DataSubscriber;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.UUID;

public class AlertSubscriber extends DataSubscriber {
    public AlertSubscriber() {
      super(RedrockPlugin.getInstance(), "alert_global");
    }

    public void handleMessage(JsonObject object) {
      UUID uuid = UUID.fromString(object.get("uuid").getAsString());
      String message = object.get("message").getAsString();
      User user = ProfilePlugin.getInstance().getUserManager().getUser(uuid);
      Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "Alert" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + colorized(message));
    }

    private String colorized(String message) {
      return ChatColor.translateAlternateColorCodes('&', message);
    }
    }
