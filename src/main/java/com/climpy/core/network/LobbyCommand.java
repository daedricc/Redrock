package com.climpy.core.network;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.redis.RedisManager;
import com.climpy.server.ServerData;
import com.climpy.server.provider.CustomServerProvider;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class LobbyCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
      Profile basicUser = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        RedisManager redisManager = RedrockPlugin.getInstance().getRedisManager();
        ServerData serverData = redisManager.getSuitableLobby();
        if (serverData == null) {
          sender.sendMessage(ChatColor.RED + "Hub sunucusu bulunamıyor, bir süre sonra tekrar deneyin.");
          return true;
        }
        CustomServerProvider customServerProvider = new CustomServerProvider(serverData.getServerName());
        sender.sendMessage(ChatColor.GREEN + "Gönderiliyorsunuz: " + ChatColor.WHITE + "Hub #" + customServerProvider.getServerID() + ChatColor.GREEN + "..");
        redisManager.sendRedisServer(basicUser.getUniqueUUID(), serverData);
        return true;
      }
}
