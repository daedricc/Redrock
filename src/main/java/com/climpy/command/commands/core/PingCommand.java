package com.climpy.command.commands.core;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.utils.C;
import com.climpy.utils.PingUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class PingCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        Player p = (Player) sender;
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(p.getName());

        if (arguments.length > 1) {
            sender.sendMessage(C.RED + "Kullanım: " + C.GOLD + "/" + label + " <oyuncuAdı>");
            return true;
        }
        if (arguments.length == 0) {
            if (sender instanceof Player) {
                sender.sendMessage(C.YELLOW + "Ping değeri: " + C.WHITE + PingUtil.getPing((Player) sender) + "ms.");
            } else {

                sender.sendMessage(C.RED + "Bu komutu yanlizca oyundan kullanabilirsiniz");
            }
        }

        if (arguments.length == 1) {
            Player target = Bukkit.getServer().getPlayerExact(arguments[0]);
            if (target == null) {
                sender.sendMessage(C.RED + "Böyle bir oyuncu yok.");
            } else {
                sender.sendMessage(C.WHITE + user.getRankType().getColor() + target.getName() + C.YELLOW + " adlı oyuncunun ping değeri: " + C.WHITE +
                        PingUtil.getPing(target) + "ms.");
            }
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length > 1) {
            return Collections.emptyList();
        }

        return null;
    }
}
