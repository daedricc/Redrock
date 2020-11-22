package com.climpy.redrock.commands.network;

import com.climpy.redrock.Locale;
import com.climpy.redrock.RedrockPlugin;
import com.climpy.redrock.commands.RedrockCommand;
import com.climpy.redrock.commands.RedrockResultCommand;
import com.climpy.redrock.redis.player.RedisPlayerAction;
import com.climpy.redrock.redis.player.RedisPlayerCache;
import com.climpy.redrock.redis.player.RedisPlayerData;
import com.climpy.torch.TorchPlugin;
import com.climpy.torch.profile.Profile;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FindCommand extends RedrockCommand implements TabCompleter {
    public FindCommand() {
        super("find");
    }

    @Override
    public RedrockResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        Profile user = TorchPlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (!user.getRank().hasPermission("torch.staff")) {
            sender.sendMessage(Locale.format(Locale.NO_PERM));
             return RedrockResultCommand.SUCCESS;
        }

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Kullanım: /" + label + " <oyuncuAdı..>");
             return RedrockResultCommand.SUCCESS;
        }

        Profile targetUser = TorchPlugin.getInstance().getProfileManager().getProfile(args[0]);
        if (targetUser == null) {
            player.sendMessage(Locale.format((Locale.PLAYER_NOT_FOUND)));
             return RedrockResultCommand.SUCCESS;
        }

        RedisPlayerCache redisPlayerCache = RedrockPlugin.getInstance().getRedisPlayerCache();
        RedisPlayerData redisPlayerData = redisPlayerCache.getRedisPlayerData(targetUser.getUniqueUUID());
        if (redisPlayerData == null) {
            sender.sendMessage(ChatColor.RED + "Oyuncu ağımız üzerinde kayıtlı durumda değil.");
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