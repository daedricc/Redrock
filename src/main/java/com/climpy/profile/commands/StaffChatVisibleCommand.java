package com.climpy.profile.commands;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffChatVisibleCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (sender instanceof Player) {
            if (!user.getRankType().isAboveOrEqual(RankType.MOD)) {
                sender.sendMessage(Locale.format(Locale.NO_PERM));
                return true;
            }

            boolean newStaffChatStatus = !user.isStaffChatVisible();
            user.setStaffChatVisible(newStaffChatStatus);
            sender.sendMessage(ChatColor.GOLD + "Staff Sohbeti " + (newStaffChatStatus ? (ChatColor.GREEN + "açıldı.") : (ChatColor.DARK_RED + "kapatıldı.")));

            user.saveProfile();
            return true;
        }
        return true;
    }
}
