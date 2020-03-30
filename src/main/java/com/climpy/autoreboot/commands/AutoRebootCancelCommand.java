package com.climpy.autoreboot.commands;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.autoreboot.AutoRebootHandler;
import com.climpy.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AutoRebootCancelCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String unparsedTime, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        if (sender instanceof Player) {
            if(!user.toPlayer().hasPermission("redrock.moderator")) {
            sender.sendMessage(Locale.format(Locale.NO_PERM));
            return true;
        }
        }

        if (!AutoRebootHandler.isRebooting()) {
            sender.sendMessage(ChatColor.RED + "Ayarlanmış bir yeniden başlatma bulunmuyor.");
        } else {
            AutoRebootHandler.cancelReboot();
            RedrockPlugin.getInstance().getServer().broadcastMessage(ChatColor.RED + "\u26a0 " + ChatColor.DARK_RED.toString() + ChatColor.STRIKETHROUGH + "------------------------" + ChatColor.RED + " \u26a0");
            RedrockPlugin.getInstance().getServer().broadcastMessage(ChatColor.RED + "Sunucuyu yeniden başlatma iptal edildi.");
            RedrockPlugin.getInstance().getServer().broadcastMessage(ChatColor.RED + "\u26a0 " + ChatColor.DARK_RED.toString() + ChatColor.STRIKETHROUGH + "------------------------" + ChatColor.RED + " \u26a0");
        }
        return true;
    }
}
