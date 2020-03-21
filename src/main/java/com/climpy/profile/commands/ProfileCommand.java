package com.climpy.profile.commands;

import com.climpy.RedrockPlugin;
import com.climpy.profile.menu.profile.StaffMenu;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.utils.C;
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
            sender.sendMessage(C.RED + "Bu işlem için " + RankType.CHAT_MOD.getDisplayName() + C.RED + " ve üzeri yetki gereklidir.");
            return true;
        }

        StaffMenu.INVENTORY.open(player);
        return true;
    }
}
