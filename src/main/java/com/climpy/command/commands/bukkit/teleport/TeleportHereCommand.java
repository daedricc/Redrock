package com.climpy.command.commands.bukkit.teleport;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportHereCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        Profile profile = RedrockPlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (!profile.getRankType().isAboveOrEqual(RankType.MOD)) {
            sender.sendMessage(Locale.format(Locale.NO_PERM));
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(C.RED + "Kullanım: /" + label + " <oyuncuAdı>");
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            Profile targetProfile = RedrockPlugin.getInstance().getProfileManager().getProfile(args[0]);
            if (target == null) {
                sender.sendMessage(Locale.format(Locale.PLAYER_OFFLINE));
                return true;
            }
            target.teleport(player);
            player.sendMessage(targetProfile.getRankType().getColor() + targetProfile.getName() + C.GOLD + " oyuncusunu kendinize ışınlandınız.");
            target.sendMessage(profile.getRankType().getColor() + profile.getName() + C.GOLD + " isimli oyuncu kendisini size ışınladı.");
            return true;
        }
        return true;
    }
}
