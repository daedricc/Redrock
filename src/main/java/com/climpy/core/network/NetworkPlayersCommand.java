package com.climpy.core.network;

import com.climpy.RedrockPlugin;
import com.climpy.redis.RedisManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class NetworkPlayersCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        RedisManager redisManager = RedrockPlugin.getInstance().getRedisManager();
        if (redisManager.getNetworkOnlinePlayers() == 0) {
            sender.sendMessage(ChatColor.RED + "Sunucuda açık kimseler yok.");
            return true;
        }
        sender.sendMessage(ChatColor.BLUE + "Sunucu üzerinde " + ChatColor.WHITE + "(" + redisManager.getNetworkOnlinePlayers() + "/" + redisManager
                .getNetworkMaxPlayers() + ")" + ChatColor.BLUE + " oyuncu açık.");
        return true;
    }
}
