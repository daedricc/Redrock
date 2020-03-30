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

public class TeleportCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        Profile profile = RedrockPlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if(!profile.toPlayer().hasPermission("redrock.moderator")) {
            sender.sendMessage(Locale.format(Locale.NO_PERM));
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(C.RED + "Kullanım: /" + label + " <oyuncuAdı> <isteğe bağlı: 2. oyuncuAdı>");
        }

        if (args.length == 1) {
            Player targetPlayer = Bukkit.getPlayer(args[0]);
            Profile targetProfile = RedrockPlugin.getInstance().getProfileManager().getProfile(args[0]);
            if (targetPlayer == null) {
                sender.sendMessage(Locale.format(Locale.PLAYER_OFFLINE));
                return true;
            }

            player.teleport(targetPlayer);
            sender.sendMessage(targetProfile.getActiveRank().getColor() + targetProfile.getName() + C.GOLD + " oyuncusuna ışınlandın.");
            return true;
        }

        if (args.length == 2) {
            Player targetPlayer = Bukkit.getPlayer(args[0]);
            Profile targetProfile = RedrockPlugin.getInstance().getProfileManager().getProfile(args[0]);

            Player target = Bukkit.getPlayer(args[1]);
            Profile targetProfile2 = RedrockPlugin.getInstance().getProfileManager().getProfile(args[1]);
            if (targetPlayer == null && target == null) {
                sender.sendMessage(Locale.format(Locale.PLAYER_OFFLINE));
                return true;
            }

            assert targetPlayer != null;
            targetPlayer.teleport(target);
            sender.sendMessage(targetProfile.getActiveRank().getColor() + targetProfile.getName() + C.GOLD + ", " + targetProfile2.getActiveRank().getColor() + target.getName() + C.GOLD + " oyuncusuna ışınlandı.");
            return true;
        }
        return true;
    }
}
