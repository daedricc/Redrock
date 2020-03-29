package com.climpy.profile.rank.commands;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.ProfileManager;
import com.climpy.profile.rank.RankType;
import com.climpy.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class ListRankCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        Player player = Bukkit.getPlayer(sender.getName());
        if (sender instanceof Player) {
            if (!user.getRankType().isAboveOrEqual(RankType.ADMIN)) {
                sender.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.ADMIN.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
                return true;
            }
        }

        String STRING_SEPARATOR = "\n";
        StringBuilder stringBuilder = new StringBuilder();

        ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
        for (RankType rankType : RankType.values()) {
            List<Profile> rankUser = (List) profileManager.getProfiles().values().stream().filter(profile -> (user.getRankType() == rankType)).collect(Collectors.toList());
            stringBuilder.append(ChatColor.GRAY + "- ").append(rankType.getPrefix()).append(rankType.getColor()).append(rankType.getDisplayName()).append(rankType.getSuffix()).append(ChatColor.GRAY).append(", ").append(rankUser.size()).append(" kişi(ler)");
            stringBuilder.append("\n");
        }

        sender.sendMessage(C.CHAT_BAR);
        sender.sendMessage(separatorCheck(stringBuilder));
        sender.sendMessage(C.CHAT_BAR);
        return true;
    }

    public static String separatorCheck(StringBuilder stringBuilder) {
        String STRING_SEPARATOR = "\n";
        return separatorCheck(stringBuilder, "\n");
    }

    public static String separatorCheck(StringBuilder stringBuilder, String STRING_SEPARATOR) {
        String toReturn = stringBuilder.toString();
        if (toReturn.endsWith(STRING_SEPARATOR)) {
            toReturn = toReturn.substring(0, toReturn.length() - STRING_SEPARATOR.length());
        }

        return toReturn;
    }

}
