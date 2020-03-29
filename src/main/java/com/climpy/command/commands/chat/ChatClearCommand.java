package com.climpy.command.commands.chat;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.utils.C;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatClearCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        if (sender instanceof Player) {
            if (!user.getRankType().isAboveOrEqual(RankType.CHAT_MOD)) {
                sender.sendMessage(Locale.format(Locale.NO_PERM));
                return true;
            }

            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                onlinePlayer.sendMessage(new String[101]);
                onlinePlayer.sendMessage(C.GOLD + "[⚠] " + C.YELLOW + "Sunucu sohbeti temizlendi.");
                if (args.length >= 1)
                    onlinePlayer.sendMessage(C.GOLD + "Neden: " + C.WHITE + StringUtils.join(args, " "));
            });
            return true;
        }
        sender.sendMessage("Bu komut sadece oyuncular tarafindan yurutulur.");
        return true;
    }
}
