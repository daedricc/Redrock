package com.climpy.profile.moderator.commands;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.moderator.FollowManager;
import com.climpy.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FollowCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        if (sender instanceof Player) {
            Profile profile = RedrockPlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());
            if(!profile.toPlayer().hasPermission("redrock.moderator")) {
            sender.sendMessage(Locale.format(Locale.NO_PERM));
            return true;
        }

            if(args.length < 1) {
                player.sendMessage(C.RED + "Kullanım: /" + label + " <oyuncuAdı..>");
                return true;
            }

            Player targetPlayer = Bukkit.getPlayer(args[0]);
            if(targetPlayer == null) {
                player.sendMessage(Locale.format(Locale.PLAYER_NOT_FOUND));
                return true;
            }

            Profile targetProfile = RedrockPlugin.getInstance().getProfileManager().getProfile(targetPlayer.getUniqueId());

            if(targetPlayer == player) {
                player.sendMessage(C.RED + "Kendini takip edemezsin.");
                return true;
            }

            FollowManager followManager = RedrockPlugin.getInstance().getFollowManager();
            for (UUID value : followManager.getFollowings(player.getUniqueId())) {
                if(targetPlayer.getUniqueId().equals(value)) {
                    player.sendMessage(C.RED + "Zaten bu kişiyi takip ediyorsun.");
                    return true;
                }
            }

            UUID followingPlayer = followManager.getFollowing(player.getUniqueId());
            if (followingPlayer != null) {
                followManager.stopFollowing(player.getUniqueId());
            }

            followManager.startFollowing(player.getUniqueId(), targetPlayer.getUniqueId());
            player.sendMessage(targetProfile.getActiveRank().getColor() + targetPlayer.getName() + C.GOLD + " adlı oyuncuyu takip ediyorsun.");
        }
        return true;
    }
}
