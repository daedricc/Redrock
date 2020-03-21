package com.climpy.redis.messages;

import com.climpy.RedrockPlugin;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
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
      Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(uuid);
      Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "Alert" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + colorized(message));
    }

    private String colorized(String message) {
      return ChatColor.translateAlternateColorCodes('&', message);
    }
    }
