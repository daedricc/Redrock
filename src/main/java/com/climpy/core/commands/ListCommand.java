package com.climpy.core.commands;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
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
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        List<StringBuilder> list = new ArrayList<>();
        StringBuilder str = new StringBuilder();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (str.length() > 0) {
                StringBuilder vanished = str.append(user.isVanishStatus() ? ChatColor.GRAY + "[V]" : "").append(user.getRankType().getColor()).append(user.getName()).append(ChatColor.GRAY + ", ");
                list.add(vanished);
             } else {
            StringBuilder vanished = str.append(user.isVanishStatus() ? ChatColor.GRAY + "[V]" : "").append(user.getRankType().getColor()).append(user.getName());
            list.add(vanished);
            }
        }

            sender.sendMessage(RankType.getAllRanks());
            sender.sendMessage("(" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + ") " + str.toString());
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments) { return Collections.emptyList(); }
}
