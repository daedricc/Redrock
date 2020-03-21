package com.climpy.core.commands;

import com.climpy.core.Core;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.utils.C;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(player.getName());
        if (!user.getRankType().isAboveOrEqual(RankType.MOD)) {
            sender.sendMessage(C.RED + "Bu işlem için " + RankType.MOD.getDisplayName() + C.RED + " ve üzeri yetki gereklidir.");
            return true;
        }

        Core.setSpawnLocation(player);
        player.sendMessage(C.GREEN + "Spawn noktası güncellendi.");
        return true;
    }
}
