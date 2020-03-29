package com.climpy.profile.rank.commands;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.redis.RedisPlayerAction;
import com.climpy.profile.redis.RedisPlayerCache;
import com.climpy.profile.redis.RedisPlayerData;
import com.climpy.redis.data.DataPublisher;
import com.climpy.redis.messages.MessageSubscriber;
import com.climpy.utils.C;
import com.climpy.utils.MiscUtils;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

public class SetRankCommand implements CommandExecutor, TabExecutor {
    private List<String> RANK_NAMES;

    public SetRankCommand() {
        this.RANK_NAMES = new ArrayList<>();
        ranksSetup();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        Player player = Bukkit.getPlayer(sender.getName());
        if (sender instanceof Player) {
            if (!player.isOp() && !user.getRankType().isAboveOrEqual(RankType.ADMIN)) {
                sender.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.ADMIN.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
                return true;
            }
        }

        if (args.length == 0) {
            sender.sendMessage(C.CHAT_BAR);
            sender.sendMessage(ChatColor.GOLD + "Rütbe Bilgileri:");
            sender.sendMessage(" ");
            sender.sendMessage(ChatColor.GRAY + "Görünen Adı: " + ChatColor.RESET + user.getRankType().getRankName());
            sender.sendMessage(ChatColor.GRAY + "Prefix: " + ChatColor.RESET + user.getRankType().getPrefix() + player.getName());
            sender.sendMessage(" ");
            List<String> permissions = user.getRankType().getPlayerAllPermissions(player);
            sender.sendMessage(ChatColor.GRAY + "Yetkiler " + ChatColor.RESET + "(" + permissions.size() + ")");
            if (!permissions.isEmpty()) {
                sender.sendMessage(permissions.toString());
            }
            sender.sendMessage(C.CHAT_BAR);
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDoğru Kullanım: /" + label + " <oyuncu-Adı> [rütbe-İsmi]"));
            return true;
        }

        Profile targetUser = RedrockPlugin.getInstance().getProfileManager().getProfile(args[0]);

        if (targetUser == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cVeritabanında &f'" + args[0] + "' &cismine sahip bir oyuncu bulunamadı."));
            return true;
        }

        RankType rankType;

        try {
            rankType = RankType.valueOf(args[1].toUpperCase(Locale.ENGLISH));
        } catch (Exception e) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cGirilen &f" + args[1] + " &crütbesi bulunamadı. "));
            return true;
        }

        if (rankType.ordinal() <= user.getRankType().ordinal() && !player.isOp() && sender instanceof Player) {
            sender.sendMessage(ChatColor.RED + "Size eşit veya yüksek bir rütbeyi başkasına veremezsiniz.");
            return true;
        }

        RankType previousRank = targetUser.getRankType();

        targetUser.setRankType(rankType);
        targetUser.setupBukkitPlayer(player);

        if (targetUser != user) {
            sender.sendMessage(MiscUtils.addNameAdditional(targetUser.getName(), true, ChatColor.GREEN) + ChatColor.GREEN + " rütbesi " + rankType.getColor() + rankType.getDisplayName() + ChatColor.GREEN + " sıralamasına " + (previousRank.isAboveOrEqual(RankType.getRankOrDefault(rankType.getRankName())) ? "düşürüldü" : "yükseltildi") + ".");
            targetUser.saveProfile();
            user.saveProfile();
        }

        RedisPlayerCache redisPlayerCache = RedrockPlugin.getInstance().getRedisPlayerCache();
        RedisPlayerData redisPlayerData = redisPlayerCache.getRedisPlayerData(targetUser.getUniqueUUID());

        if (redisPlayerData.getRedisPlayerAction() == RedisPlayerAction.JOINING_SERVER) {
            DataPublisher dataPublisher = RedrockPlugin.getInstance().getRedisConnection().getDataPublisher();
            JsonObject messageObject = new JsonObject();
            MessageSubscriber.MessageType messageType = MessageSubscriber.MessageType.PLAYER;
            messageObject.addProperty("type", messageType.getInternalName());
            messageObject.addProperty("message", ChatColor.GRAY + "[Network] " + ChatColor.GREEN + "Rütbeniz " + rankType.getColor() + rankType.getDisplayName() + ChatColor.GREEN + "'a " + (
                    previousRank.isAboveOrEqual(RankType.getRankOrDefault(rankType.getRankName())) ? "düşürüldü." : "yükseltildi") + ".");
            messageObject.addProperty("value", targetUser.getUniqueUUID().toString());
            dataPublisher.write("message", messageObject);
            targetUser.saveProfile();
            user.saveProfile();
        }

        targetUser.saveProfile();
        user.saveProfile();
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        Validate.notNull(sender, "Sender boolamaz");
        Validate.notNull(args, "Arguments boolamaz");
        Validate.notNull(label, "Alias boolamaz");
        if (args.length == 1) {
            List<String> onlinePlayers = new ArrayList<>();
            for (Profile user : RedrockPlugin.getInstance().getProfileManager().getDatabaseProfiles()) {
                if (user.getName().toLowerCase().startsWith(args[0].toLowerCase()))
                    onlinePlayers.add(user.getName());
            }
            return onlinePlayers;
        }
        if (args.length == 2)
            return (List<String>) StringUtil.copyPartialMatches(args[1], this.RANK_NAMES, new ArrayList(this.RANK_NAMES.size()));
        return ImmutableList.of();
    }

    private void ranksSetup() {
        if (!this.RANK_NAMES.isEmpty())
            RedrockPlugin.getInstance().getLogger().log(Level.WARNING, "Rutbeler hala kurulmadi.");
        for (RankType rank : RankType.values())
            this.RANK_NAMES.add(rank.getRankName());
        Collections.sort(this.RANK_NAMES);
    }
}
