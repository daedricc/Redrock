package com.climpy.command.commands.bukkit;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.utils.MiscUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        StringBuilder str = new StringBuilder();

        for (Player player : Bukkit.getOnlinePlayers()) {
            Profile profile = RedrockPlugin.getInstance().getProfileManager().getProfile(player.getName());
            String displayName;
            if(profile == null) {
                displayName = player.getName();
            } else {
                displayName = profile.getRankType().getColor() + profile.getName();
            }

            str.append(displayName).append(ChatColor.GRAY).append(", ");
        }

        sender.sendMessage(RankType.getAllRanks());
        sender.sendMessage("(" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + ") " + MiscUtils.separatorCheck(str.toString(), ", "));
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments) {
        return Collections.emptyList();
    }
}
