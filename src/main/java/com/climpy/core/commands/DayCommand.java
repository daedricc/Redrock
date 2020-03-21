package com.climpy.core.commands;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DayCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        Player player = Bukkit.getPlayer(sender.getName());
        if (sender instanceof Player) {
            if (!user.getRankType().isAboveOrEqual(RankType.MOD)) {
                sender.sendMessage(C.RED + "Bu işlem için " + RankType.MOD.getDisplayName() + C.RED + " ve üzeri yetki gereklidir.");
                return true;
            }
        }

        if(args.length < 1) {
            player.sendMessage(C.RED + "&cKullanım: /" + label + " [all/player]");
            return true;
        }

        if(args[0].equals("all")) {
            player.getWorld().setFullTime(6000L);
            player.sendMessage(C.GREEN + "Tüm oyuncular için şimdi gündüz vakti.");
            return true;
        }

        if(args[0].equals("player")) {
            player.setPlayerTime(6000L, false);
            player.sendMessage(C.GREEN + "Sadece sizin için şimdi gündüz vakti.");
            return true;
        }
        return true;
    }
}
