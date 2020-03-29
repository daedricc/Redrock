package com.climpy.profile.moderator.commands;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.moderator.StaffManager;
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
            Profile profile = RedrockPlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());
            if (!profile.getRankType().isAboveOrEqual(RankType.MOD)) {
                sender.sendMessage(Locale.format(Locale.NO_PERM));
                return true;
            }

            StaffManager staffManager = RedrockPlugin.getInstance().getStaffManager();

            boolean newStaffModeStatus = !profile.isStaffMode();
            staffManager.setStaffMode(profile.getUniqueUUID(), newStaffModeStatus);
            sender.sendMessage(ChatColor.GOLD + "Staff Modu " + (newStaffModeStatus ? (ChatColor.GREEN + "açıldı.") : (ChatColor.DARK_RED + "kapatıldı.")));
            return true;
        }
        sender.sendMessage("Bu islem sadece oyundan yurutelebilir.");
        return true;
    }
}
