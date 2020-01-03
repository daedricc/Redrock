package com.climpy.commands.commands;

import com.climpy.Locale;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.utils.C;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FindCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getUniqueId());
        if (!user.getRankType().isAboveOrEqual(RankType.MOD)) {
            sender.sendMessage(C.RED + "Bu işlem için " + RankType.MOD.getColor() + RankType.MOD.getDisplayName() + C.RED + " ve üzeri yetki gereklidir.");
            return true;
        }

        if(args.length < 1) {
            player.sendMessage(ChatColor.RED + "Kullanım: /" + label + " <oyuncuAdı..>");
            return true;
        }

        User targetUser = ProfilePlugin.getInstance().getUserManager().getUser(args[0]);
        if(targetUser == null) {
            player.sendMessage(Locale.format((Locale.PLAYER_NOT_FOUND)));
            return true;
        }

        Player targetPlayer = Bukkit.getPlayer(targetUser.getUniqueUUID());
        if(targetPlayer == null || !targetUser.isOnlineStatus()) {
            player.sendMessage(Locale.format(Locale.PLAYER_OFFLINE));
            return true;
        }

        player.sendMessage(C.translate("&f" + targetUser.getName() + " &6şu anda şu sunucuda &f" + targetUser.getServerName()));
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        Validate.notNull(sender, "Sender boş olamaz");
        Validate.notNull(args, "Arguments boş olamaz");
        Validate.notNull(label, "Alias boş olamaz");
        if (args.length == 1) {
            List<String> onlinePlayers = new ArrayList<>();
            for (User user : ProfilePlugin.getInstance().getUserManager().getDatabaseUsers()) {
                if (user.getName().toLowerCase().startsWith(args[0].toLowerCase()))
                    onlinePlayers.add(user.getName());
            }
            return onlinePlayers;
        }
        return ImmutableList.of();
    }


}