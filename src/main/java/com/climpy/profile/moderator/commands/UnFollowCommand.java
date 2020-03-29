package com.climpy.profile.moderator.commands;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.moderator.FollowManager;
import com.climpy.profile.rank.RankType;
import com.climpy.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UnFollowCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        if (sender instanceof Player) {
            Profile profile = RedrockPlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());
            if (!profile.getRankType().isAboveOrEqual(RankType.MOD)) {
                sender.sendMessage(Locale.format(Locale.NO_PERM));
                return true;
            }

            FollowManager followManager = RedrockPlugin.getInstance().getFollowManager();
            UUID following = followManager.getFollowing(player.getUniqueId());
            if (following == null) {
                sender.sendMessage(ChatColor.RED + "Herhangi birini takip etmiyorsun.");
                return true;
            }

            Profile profile1 = RedrockPlugin.getInstance().getProfileManager().getProfile(following);
            followManager.stopFollowing(player.getUniqueId());
            player.sendMessage(C.WHITE + profile1.getName() + C.GOLD + " adlı oyuncuyu artık takip etmiyorsun.");
        }
        return true;
    }
}
