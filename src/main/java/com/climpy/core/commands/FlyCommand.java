package com.climpy.core.commands;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.utils.C;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
    if (s.equalsIgnoreCase("fly")) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(C.RED + "Konsol bunu kullanamaz");
            return true;
        }

        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        Player player = (Player) sender;

        if (!user.getRankType().isAboveOrEqual(RankType.VIPPLUS)) {
            sender.sendMessage(C.RED + "Bu işlem için " + RankType.VIPPLUS.getDisplayName() + C.RED + " ve üzeri rütbe gereklidir.");
            return true;
        }

        if (args.length == 0) {
            player.setAllowFlight(!player.getAllowFlight());
            player.sendMessage(player.getAllowFlight() ? C.GOLD + "Uçuş modu " + C.WHITE + "açıldı." : C.GOLD + "Uçuş modu " + C.WHITE + "kapatıldı.");
        }
        else if (args[0].equalsIgnoreCase("on")) {
            if (player.getAllowFlight()) {
                player.sendMessage(C.RED + "Zaten uçuş modu açık.");
            } else {
                player.setAllowFlight(true);
                player.sendMessage(C.GOLD + "Uçuş modu " + C.WHITE + "açıldı.");
            }
        } else if (args[0].equalsIgnoreCase("off")) {
            if (!player.getAllowFlight()) {
                player.sendMessage(C.RED + "Zaten uçuş modu kapalı.");
            } else {
                player.setAllowFlight(false);
                player.sendMessage(C.GOLD + "Uçuş modu " + C.WHITE + "kapatıldı.");
                }
            }
        return true;
        }
    return false;
    }
}
