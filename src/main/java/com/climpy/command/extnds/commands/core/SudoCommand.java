package com.climpy.command.extnds.commands.core;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
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
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (!user.toPlayer().hasPermission("redrock.mod")) {
            sender.sendMessage(Locale.format(Locale.NO_PERM));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Kullanım: /" + label + " <oyuncuAdı/herkes> [Komut..]");
            return true;
        }

        String executingCommand = StringUtils.join((Object[]) args, " ", 1, args.length).replace("/", "");
        if (args[0].equals("hepsi") || args[0].equals("herkes") || args[0].equals("all")) {
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.performCommand(executingCommand));
            sender.sendMessage(ChatColor.GOLD + "Tüm oyunculara " + ChatColor.WHITE + executingCommand + ChatColor.GOLD + " komutu girdirildi.");
            return true;
        }

        Player targetPlayer = Bukkit.getPlayerExact(args[0]);
        String chat = StringUtils.join((Object[]) args, " ", 2, args.length);
        if(args[1].equals("chat")) {
            sender.sendMessage(ChatColor.GOLD + targetPlayer.getName() + " oyuncusuna " + ChatColor.WHITE + chat + ChatColor.GOLD + " mesajı girdirildi.");
            targetPlayer.chat(chat);
            return true;
        }

        if (targetPlayer == null) {
            sender.sendMessage(Locale.format(Locale.PLAYER_NOT_FOUND));
            return true;
        }

        targetPlayer.performCommand(executingCommand);
        sender.sendMessage(ChatColor.GOLD + targetPlayer.getName() + " oyuncusuna " + ChatColor.WHITE + executingCommand + ChatColor.GOLD + " komutu girdirildi.");
        return true;
    }
}
