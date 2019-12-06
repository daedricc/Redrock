package com.climpy.commands.commands;

import com.climpy.commands.Core;
import com.climpy.utils.C;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        Core.setSpawnLocation(player);
        player.sendMessage(C.GREEN + "Spawn noktası güncellendi.");
        return true;
    }
}
