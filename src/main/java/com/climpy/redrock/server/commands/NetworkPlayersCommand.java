package com.climpy.redrock.server.commands;

import com.climpy.redrock.RedrockPlugin;
import com.climpy.redrock.commands.RedrockCommand;
import com.climpy.redrock.commands.RedrockResultCommand;
import com.climpy.redrock.redis.RedisManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class NetworkPlayersCommand extends RedrockCommand {
    public NetworkPlayersCommand() {
        super("network", "Tüm sunucudaki oyuncuları gösterir.");
    }

    @Override
    public RedrockResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        RedisManager redisManager = RedrockPlugin.getInstance().getRedisManager();
        if (redisManager.getNetworkOnlinePlayers() == 0) {
            sender.sendMessage(ChatColor.RED + "Sunucuda açık kimseler yok.");
            return RedrockResultCommand.SUCCESS;
        }

        sender.sendMessage(ChatColor.BLUE + "Sunucu üzerinde " + ChatColor.WHITE + "(" + redisManager.getNetworkOnlinePlayers() + "/" + redisManager
                .getNetworkMaxPlayers() + ")" + ChatColor.BLUE + " oyuncu açık.");
        return RedrockResultCommand.SUCCESS;
    }
}
