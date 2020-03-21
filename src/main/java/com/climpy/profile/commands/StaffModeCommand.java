package com.climpy.profile.commands;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffModeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        Player player = Bukkit.getPlayer(sender.getName());
        if (sender instanceof Player) {
            Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());
            if (!user.getRankType().isAboveOrEqual(RankType.MOD)) {
                sender.sendMessage(ChatColor.RED + "Bu işlem için " + RankType.MOD
                        .getColor() + RankType.MOD.getDisplayName() + ChatColor.RED + " ve üzeri rütbe gereklidir.");

                return true;
            }

            boolean newStaffModeStatus = !user.isStaffMode();
            user.setStaffMode(newStaffModeStatus);
       //     RedrockPlugin.getInstance().getStaffModeListener().setStaffMode(player, newStaffModeStatus);
            sender.sendMessage(ChatColor.GOLD + "Staff Modu " + (newStaffModeStatus ? (ChatColor.GREEN + "açıldı.") : (ChatColor.DARK_RED + "kapatıldı.")));

            user.saveProfile();
            return true;
        }
        sender.sendMessage("Bu islem sadece oyundan yurutelebilir.");
        return true;
    }
}
