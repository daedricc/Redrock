package com.climpy.redis.messages;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.ProfileManager;
import com.climpy.profile.rank.RankType;
import com.climpy.redis.data.DataSubscriber;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ChatSubscriber extends DataSubscriber {
  public ChatSubscriber() {
    super(RedrockPlugin.getInstance(), "staff_chat");
  }
  
  public void handleMessage(JsonObject object) {
    UUID uuid = UUID.fromString(object.get("uuid").getAsString());
    String serverName = object.get("serverName").getAsString();
    String message = object.get("message").getAsString();
    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      Profile onlineUser = RedrockPlugin.getInstance().getProfileManager().getProfile(onlinePlayer.getUniqueId());
      RankType rankType = onlineUser.getRankType();
      if (rankType.isAboveOrEqual(RankType.CHAT_MOD))
        onlinePlayer.sendMessage(setMessageFormat(serverName, setPrefix(uuid), message)); 
    } 
  }
  
  private String setMessageFormat(String server, String playerNameFormat, String message) {
    return ChatColor.translateAlternateColorCodes('&', ChatColor.BLUE + "[Personel] " + ChatColor.AQUA + "(" + server + ") " + playerNameFormat + ChatColor.WHITE + ": " + ChatColor.RESET) +
      
      ChatColor.translateAlternateColorCodes('&', message);
  }

  private String setPrefix(UUID eventPlayerUUID) {
    ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
    Profile eventUser = RedrockPlugin.getInstance().getProfileManager().getProfile(eventPlayerUUID);
    eventUser.getRankType();
    return eventUser.getRankType().getColor() + eventUser.getName() + ChatColor.RESET;
  }
}
