package com.climpy.command.commands.core;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.redis.RedisPlayerAction;
import com.climpy.profile.redis.RedisPlayerCache;
import com.climpy.profile.redis.RedisPlayerData;
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

public class WhoisCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        Player player = Bukkit.getPlayer(sender.getName());
        if (sender instanceof Player) {
            if (!user.getRankType().isAboveOrEqual(RankType.MOD)) {
                sender.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.MOD.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
                return true;
            }
        }

        if (args.length < 1) {
            sender.sendMessage(C.translate("&cDoğru Kullanım: /" + s + " <oyuncuAdı>"));
            return true;
        }

        Profile targetUser = RedrockPlugin.getInstance().getProfileManager().getProfile(args[0]);

        if (targetUser == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cVeritabanında &f'" + args[0] + "' &cismine sahip bir oyuncu bulunamadı."));
            return true;
        }

        RedisPlayerCache redisPlayerCache = RedrockPlugin.getInstance().getRedisPlayerCache();
        RedisPlayerData redisPlayerData = redisPlayerCache.getRedisPlayerData(targetUser.getUniqueUUID());
        if (redisPlayerData == null) {
            sender.sendMessage(ChatColor.RED + "Oyuncu ağımız üzerinde kayıtlı durumda değil.");
            return true;
        }

        sender.sendMessage(C.CHAT_BAR);
        sender.sendMessage(C.translate("&9Kullanıcı: &f" + targetUser.getName()));
        sender.sendMessage(C.translate("&9UUID: &f" + targetUser.getUniqueUUID()));
        sender.sendMessage(C.translate("&9Mevcut IP: &f" + targetUser.getCurrentAddress()));
        sender.sendMessage(C.translate("&9Tüm IP adresleri: &f" + targetUser.getIpAddresses()));
        sender.sendMessage(C.translate(" "));
        sender.sendMessage(C.translate("&9Rütbe: &f" + targetUser.getRankType().getRankName()));
        sender.sendMessage(C.translate("&9Sembol: &f" + targetUser.getSymbolType().getDisplayName()));
        sender.sendMessage(C.translate(" "));
        sender.sendMessage(C.translate("&9Sunucu: &f" + redisPlayerData.getLastSeenServer()));
        sender.sendMessage(C.translate("&9Kredi Miktarı: &f" + targetUser.getCredit()));
        sender.sendMessage(C.translate("&9Aktiflik Durumu: &f" + (redisPlayerData.getRedisPlayerAction() == RedisPlayerAction.JOINING_SERVER ? "Aktif" : "Kapalı")));
        sender.sendMessage(C.translate(" "));
        sender.sendMessage(C.translate("&9İlk giriş: &f" + targetUser.getFirstLoginTime()));
        sender.sendMessage(C.translate("&9Son giriş: &f" + targetUser.getLastLoginTime()));
        sender.sendMessage(C.translate(" "));
        sender.sendMessage(C.translate("&9Freeze Durumu: &f" + (targetUser.isFrozenStatus() ? "&aAçık" : "&cKapalı")));
        sender.sendMessage(C.translate("&9Staff Modu: &f" + (targetUser.isStaffMode() ? "&aAçık" : "&cKapalı")));
        sender.sendMessage(C.CHAT_BAR);
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
