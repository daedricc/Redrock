package com.climpy.commands.commands;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.utils.C;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatClearCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());
        if (sender instanceof Player) {
        if (!user.getRankType().isAboveOrEqual(RankType.CHAT_MOD)) {
            sender.sendMessage(ChatColor.RED + "Bu işlem için " + RankType.CHAT_MOD.getColor() + RankType.CHAT_MOD.getDisplayName() + ChatColor.RED + " ve üzeri rütbe gereklidir.");
            return true;
        }

        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            onlinePlayer.sendMessage(new String[101]);
            onlinePlayer.sendMessage(C.GOLD + "[⚠] " + C.YELLOW + "Sunucu sohbeti temizlendi.");
            if (args.length >= 1) onlinePlayer.sendMessage(C.GOLD + "Neden: " + C.WHITE + StringUtils.join((Object[])args, " "));
            });
        return true;
        }
        sender.sendMessage("Bu komut sadece oyuncular tarafindan yurutulur.");
        return true;
    }
}
