package com.climpy.command.network;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.redis.RedisPlayerAction;
import com.climpy.profile.redis.RedisPlayerCache;
import com.climpy.profile.redis.RedisPlayerData;
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
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (!user.getRankType().isAboveOrEqual(RankType.MOD)) {
            sender.sendMessage(Locale.format(Locale.NO_PERM));
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Kullanım: /" + label + " <oyuncuAdı..>");
            return true;
        }

        Profile targetUser = RedrockPlugin.getInstance().getProfileManager().getProfile(args[0]);
        if (targetUser == null) {
            player.sendMessage(Locale.format((Locale.PLAYER_NOT_FOUND)));
            return true;
        }

        RedisPlayerCache redisPlayerCache = RedrockPlugin.getInstance().getRedisPlayerCache();
        RedisPlayerData redisPlayerData = redisPlayerCache.getRedisPlayerData(targetUser.getUniqueUUID());
        if (redisPlayerData == null) {
            sender.sendMessage(ChatColor.RED + "Oyuncu ağımız üzerinde kayıtlı durumda değil.");
            return true;
        }

        ChatColor color = redisPlayerData.getRedisPlayerAction() == RedisPlayerAction.JOINING_SERVER ? ChatColor.GREEN : ChatColor.RED;
        sender.sendMessage(new String[]{
                ChatColor.BLUE + targetUser.getName() + " için en sonki konum " + color + redisPlayerData.getLastSeenServer() + ChatColor.BLUE + ".",
                ChatColor.BLUE + "Güncelleme zamanı: " + ChatColor.YELLOW + redisPlayerData.getLastSeenTimeAgo()
        });
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        Validate.notNull(sender, "Sender boş olamaz");
        Validate.notNull(args, "Arguments boş olamaz");
        Validate.notNull(label, "Alias boş olamaz");
        if (args.length == 1) {
            List<String> onlinePlayers = new ArrayList<>();
            for (Profile user : RedrockPlugin.getInstance().getProfileManager().getDatabaseProfiles()) {
                if (user.getName().toLowerCase().startsWith(args[0].toLowerCase()))
                    onlinePlayers.add(user.getName());
            }
            return onlinePlayers;
        }
        return ImmutableList.of();
    }


}