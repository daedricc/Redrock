package com.climpy.redrock.server.commands;

import com.climpy.redrock.RedrockPlugin;
import com.climpy.redrock.commands.RedrockCommand;
import com.climpy.redrock.commands.RedrockResultCommand;
import com.climpy.redrock.redis.player.RedisPlayerAction;
import com.climpy.redrock.redis.player.RedisPlayerCache;
import com.climpy.redrock.redis.player.RedisPlayerData;
import com.climpy.torch.TorchPlugin;
import com.climpy.torch.locale.Locale;
import com.climpy.torch.profile.Profile;
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

public class FindCommand extends RedrockCommand implements TabCompleter {
    public FindCommand() {
        super("find", "Oyuncuyu sunucuda arar.");
    }

    @Override
    public RedrockResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        Profile profile = TorchPlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (!profile.toPlayer().hasPermission("redrock.server.find") || (!profile.toPlayer().hasPermission("redrock.server.*"))) {
            return RedrockResultCommand.INVALID_PERMISSION;
        }

        if (args.length < 1) {
            setUsage(label + " <oyuncuAdı..>");
            return RedrockResultCommand.INVALID_USAGE;
        }

        Profile targetUser = TorchPlugin.getInstance().getProfileManager().getProfile(args[0]);
        if (targetUser == null) {
            player.sendMessage(Locale.message(profile, "INVALID_PLAYER", args[0]));
            return RedrockResultCommand.SUCCESS;
        }

        RedisPlayerCache redisPlayerCache = RedrockPlugin.getInstance().getRedisPlayerCache();
        RedisPlayerData redisPlayerData = redisPlayerCache.getRedisPlayerData(targetUser.getUniqueUUID());
        if (redisPlayerData == null) {
            player.sendMessage(Locale.message(profile, "INVALID_PLAYER", args[0]));
            return RedrockResultCommand.SUCCESS;
        }

        ChatColor color = redisPlayerData.getRedisPlayerAction() == RedisPlayerAction.JOINING_SERVER ? ChatColor.GREEN : ChatColor.RED;
        sender.sendMessage(new String[]{
                ChatColor.BLUE + targetUser.getName() + " için en sonki konum " + color + redisPlayerData.getLastSeenServer() + ChatColor.BLUE + ".",
                ChatColor.BLUE + "Güncelleme zamanı: " + ChatColor.YELLOW + redisPlayerData.getLastSeenTimeAgo()
        });
        return RedrockResultCommand.SUCCESS;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        Validate.notNull(sender, "Sender boş olamaz");
        Validate.notNull(args, "Arguments boş olamaz");
        Validate.notNull(label, "Alias boş olamaz");
        if (args.length == 1) {
            List<String> onlinePlayers = new ArrayList<>();
            for (Profile user : TorchPlugin.getInstance().getProfileManager().getDatabaseProfiles()) {
                if (user.getName().toLowerCase().startsWith(args[0].toLowerCase()))
                    onlinePlayers.add(user.getName());
            }
            return onlinePlayers;
        }
        return ImmutableList.of();
    }
}