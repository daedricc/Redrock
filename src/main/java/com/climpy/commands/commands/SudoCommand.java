package com.climpy.commands.commands;

import com.climpy.Locale;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.utils.C;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SudoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getUniqueId());
        if (!user.getRankType().isAboveOrEqual(RankType.MOD)) {
            sender.sendMessage(C.RED + "Bu işlem için " + RankType.MOD.getColor() + RankType.MOD.getDisplayName() + C.RED + " ve üzeri yetki gereklidir.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Kullanım: /" + label + " <oyuncuAdı/herkes> [Komut..]");
            return true;
        }

        String executingCommand = StringUtils.join((Object[])args, " ", 1, args.length).replace("/", "");
        if (args[0].equals("hepsi") || args[0].equals("herkes") || args[0].equals("all")) {
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.performCommand(executingCommand));
            sender.sendMessage(ChatColor.GOLD + "Tüm oyunculara " + ChatColor.WHITE + executingCommand + ChatColor.GOLD + " komutu girdirildi.");
            return true;
        }
        Player targetPlayer = Bukkit.getPlayerExact(args[0]);
        if (targetPlayer == null) {
            sender.sendMessage(Locale.format(Locale.PLAYER_NOT_FOUND));
            return true;
        }

        targetPlayer.performCommand(executingCommand);
            sender.sendMessage(ChatColor.GOLD + targetPlayer.getName() + " oyuncusuna " + ChatColor.WHITE + executingCommand + ChatColor.GOLD + " komutu girdirildi.");
        return true;
    }
}
