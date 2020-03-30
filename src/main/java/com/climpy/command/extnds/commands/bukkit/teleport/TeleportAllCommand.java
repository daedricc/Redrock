package com.climpy.command.extnds.commands.bukkit.teleport;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportAllCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        Profile profile = RedrockPlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if(!profile.toPlayer().hasPermission("redrock.moderator")) {
            sender.sendMessage(Locale.format(Locale.NO_PERM));
            return true;
        }

        if (args.length > 0) {
            sender.sendMessage(C.RED + "Kullanım: /" + label);
            return true;
        }

        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            if (onlinePlayers != player) {
                onlinePlayers.teleport(player);
                onlinePlayers.sendMessage(profile.getActiveRank().getColor() + profile.getName() + C.GOLD + ", herkesi kendine ışınlandı.");
            }
        }

        player.sendMessage(C.GREEN + "Tüm oyuncular size ışınlandı.");
        return true;
    }
}
