package com.climpy.redis.messages;

import com.climpy.RedrockPlugin;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.profile.user.UserManager;
import com.climpy.redis.data.DataSubscriber;
import com.google.gson.JsonObject;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatSubscriber extends DataSubscriber {
  public ChatSubscriber() {
    super(RedrockPlugin.getInstance(), "staff_chat");
  }
  
  public void handleMessage(JsonObject object) {
    UUID uuid = UUID.fromString(object.get("uuid").getAsString());
    String serverName = object.get("serverName").getAsString();
    String message = object.get("message").getAsString();
    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      User onlineUser = ProfilePlugin.getInstance().getUserManager().getUser(onlinePlayer.getUniqueId());
      RankType rankType = onlineUser.getRankType();
      if (rankType.isAboveOrEqual(RankType.CHAT_MOD) && onlineUser.isStaffChatVisible())
        onlinePlayer.sendMessage(setMessageFormat(serverName, setPrefix(uuid), message)); 
    } 
  }
  
  private String setMessageFormat(String server, String playerNameFormat, String message) {
    return ChatColor.translateAlternateColorCodes('&', ChatColor.BLUE + "[Personel] " + ChatColor.AQUA + "(" + server + ") " + playerNameFormat + ChatColor.WHITE + ": " + ChatColor.RESET) +
      
      ChatColor.translateAlternateColorCodes('&', message);
  }
  
  private String setPrefix(UUID eventPlayerUUID) {
    UserManager userManager = ProfilePlugin.getInstance().getUserManager();
    User eventUser = userManager.getUser(eventPlayerUUID);
    eventUser.getRankType();
    return eventUser.getSymbolType().getPrefix() + " " + eventUser.getRankType().getColor() + eventUser.getName() + ChatColor.RESET;
  }
}
