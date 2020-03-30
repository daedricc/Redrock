package com.climpy.autoreboot.commands;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.autoreboot.AutoRebootHandler;
import com.climpy.profile.Profile;
import com.climpy.utils.C;
import com.climpy.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class AutoRebootCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        if (sender instanceof Player) {
            if(!user.toPlayer().hasPermission("redrock.moderator")) {
            sender.sendMessage(Locale.format(Locale.NO_PERM));
            return true;
        }
        }

        if (args.length < 1) {
            player.sendMessage(C.RED + "Kullanım: /" + label + " [zaman..]");
            return true;
        }

        int time = TimeUtils.parseTime(args[0]);
        AutoRebootHandler.rebootServer(time, TimeUnit.SECONDS);
        sender.sendMessage(ChatColor.YELLOW + "Sunucuyu yeniden başlatma başlatıldı.");
        return true;
    }
}
