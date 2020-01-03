package com.climpy.commands.commands;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.redis.RedisManager;
import com.climpy.redis.data.DataPublisher;
import com.climpy.redis.messages.WhitelistSubscriber;
import com.climpy.server.ServerData;
import com.climpy.utils.C;
import com.google.gson.JsonObject;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;

public class WhitelistCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());
      if (!user.getRankType().isAboveOrEqual(RankType.MOD)) {
        sender.sendMessage(C.RED + "Bu işlem için " + RankType.MOD.getDisplayName() + C.RED + " ve üzeri yetki gereklidir.");
        return true;
      }
      if (args.length < 2) {
        String STRING_SEPARATOR = "\n";
        String stringBuilder = ChatColor.RED + "Doğru Kullanım /whitelist (add:remove) [oyuncuAdı]";
        sender.sendMessage(stringBuilder);
        return true;
      }
      DataPublisher dataPublisher = RedrockPlugin.getInstance().getRedisConnection().getDataPublisher();
      JsonObject whitelistObject = new JsonObject();
      if (args[0].equals("add") || args[0].equals("ekle") || args[0].equals("remove") || args[0].equals("delete") || args[0].equals("sil")) {
        User targetUser = ProfilePlugin.getInstance().getUserManager().getUser(args[1]);
        if (targetUser == null) {
          sender.sendMessage(Locale.format(Locale.PLAYER_NOT_FOUND));
          return true;
        }
        if (args[0].equals("add") || args[0].equals("ekle")) {
          WhitelistSubscriber.WhitelistType whitelistType = WhitelistSubscriber.WhitelistType.ADD;
          whitelistObject.addProperty("type", whitelistType.getInternalName());
          whitelistObject.addProperty("target", targetUser.getUniqueUUID().toString());
          sender.sendMessage(ChatColor.GREEN + "[Global] '" + targetUser.getName() + "' beyaz-listeye eklendi.");
          RedrockPlugin.getInstance().getLogger().log(Level.INFO, "[Whitelist] " + targetUser.getName() + " global beyaz-listeye eklendi.");
        } else if (args[0].equals("remove") || args[0].equals("delete") || args[0].equals("sil")) {
          WhitelistSubscriber.WhitelistType whitelistType = WhitelistSubscriber.WhitelistType.REMOVE;
          whitelistObject.addProperty("type", whitelistType.getInternalName());
          whitelistObject.addProperty("target", targetUser.getUniqueUUID().toString());
          sender.sendMessage(ChatColor.RED + "[Global] '" + targetUser.getName() + "' beyaz-listeden silindi.");
          RedrockPlugin.getInstance().getLogger().log(Level.INFO, "[Whitelist] " + targetUser.getName() + " global beyaz-listeden silindi.");
        }
      } else if (args[0].equals("on") || args[0].equals("open") || args[0].equals("aç") || args[0].equals("off") || args[0].equals("disable") || args[0].equals("kapat")) {
        RedisManager redisManager = RedrockPlugin.getInstance().getRedisManager();
      ServerData serverData = redisManager.getServerByName(args[1]);
      if (serverData == null) {
        sender.sendMessage(ChatColor.RED + "Girilen sunucu bulunamadı.");
        return true;
      }
      if (args[0].equals("on") || args[0].equals("open") || args[0].equals("aç")) {
      if (serverData.isWhitelisted()) {
        sender.sendMessage(ChatColor.RED + serverData.getServerName() + " zaten beyaz-listede modunda.");
        return true;
      }
      WhitelistSubscriber.WhitelistType whitelistType = WhitelistSubscriber.WhitelistType.ON;
      whitelistObject.addProperty("type", whitelistType.getInternalName());
      whitelistObject.addProperty("target", serverData.getServerName());
      sender.sendMessage(ChatColor.BLUE + "Sunucu " + serverData.getServerName() + " beyaz-listeye girdi.");
      RedrockPlugin.getInstance().getLogger().log(Level.INFO, "[Whitelist] " + serverData.getServerName() + " için beyaz-liste açıldı.");
    } else if (args[0].equals("off") || args[0].equals("disable") || args[0].equals("kapat")) {
      if (!serverData.isWhitelisted()) {
        sender.sendMessage(ChatColor.RED + serverData.getServerName() + " zaten beyaz-listede değil.");
        return true;
      }
      WhitelistSubscriber.WhitelistType whitelistType = WhitelistSubscriber.WhitelistType.OFF;
      whitelistObject.addProperty("type", whitelistType.getInternalName());
      whitelistObject.addProperty("target", serverData.getServerName());
      sender.sendMessage(ChatColor.BLUE + "Sunucu " + serverData.getServerName() + " beyaz-listeden kaldırıldı.");
      RedrockPlugin.getInstance().getLogger().log(Level.INFO, "[Whitelist] " + serverData.getServerName() + " için beyaz-liste kaldırıldı.");
      }
    }
    if (args[0].equals("reload") || args[0].equals("yenile")) {
          RedrockPlugin.getInstance().getServer().reloadWhitelist();
          sender.sendMessage(ChatColor.GREEN + "Veriler beyaz-liste için yeniden yüklendi.");
          RedrockPlugin.getInstance().getLogger().log(Level.WARNING, "[Whitelist] Veriler beyaz-liste sunucusu için yeniden yüklendi.");
          }
          dataPublisher.write("whitelist", whitelistObject);
          return true;
    }
  }
