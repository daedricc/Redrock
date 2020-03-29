package com.climpy.profile.commands;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.menu.profile.StaffMenu;
import com.climpy.profile.rank.RankType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ProfileCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        Player player = Bukkit.getPlayer(sender.getName());
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (!user.getRankType().isAboveOrEqual(RankType.CHAT_MOD)) {
            sender.sendMessage(Locale.format(Locale.NO_PERM));
            return true;
        }

        StaffMenu.INVENTORY.open(player);
        return true;
    }
}
