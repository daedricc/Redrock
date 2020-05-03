package com.climpy.redrock.redis.messages;

import com.climpy.redrock.RedrockPlugin;
import com.climpy.redrock.redis.data.DataSubscriber;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class AlertSubscriber extends DataSubscriber {
    public AlertSubscriber() {
        super(RedrockPlugin.getInstance(), "alert_global");
    }

    public void handleMessage(JsonObject object) {
        String message = object.get("message").getAsString();
        Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "Alert" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + colorized(message));
    }

    private String colorized(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
