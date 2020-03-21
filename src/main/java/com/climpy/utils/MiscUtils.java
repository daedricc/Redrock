package com.climpy.utils;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.punishments.Punishment;
import com.climpy.punishments.PunishmentManager;
import com.climpy.report.ReportManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MiscUtils {
  public static boolean patternCheck(String message, Pattern pattern) {
    Matcher matcher = pattern.matcher(message);
    if (matcher.find())
      return true;
    return false;
  }

  public static String generateReportRandomID() {
    ReportManager reportManager = RedrockPlugin.getInstance().getReportManager();
    String generatedRandomID = generateRandomIDMethod();
    
    if (reportManager.getReport(generatedRandomID) != null) {
      return generatedRandomID;
    }

    return generatedRandomID;
  }

  public static String generatePunishmentRandomID() {
    String generatedRandomID = generateRandomIDMethod();

    PunishmentManager punishmentManager = RedrockPlugin.getInstance().getPunishmentManager();
    Map<UUID, List<Punishment>> allPunishments = punishmentManager.getDatabasePunishments();

    for (List<Punishment> punishments : allPunishments.values()) {
      for (Punishment punishment : punishments) {
        if (punishment.getPunishmentID().equals(generatedRandomID)) {
          return generateRandomIDMethod();
        }
      }
    }

    return generatedRandomID;
  }
  
  private static String generateRandomIDMethod() {
    String SALT_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    StringBuilder stringBuilder = new StringBuilder();
    Random random = new Random();
    
    while (stringBuilder.length() < 10) {
      int index = (int)(random.nextFloat() * "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".length());
      stringBuilder.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".charAt(index));
    } 
    
    return stringBuilder.toString();
  }

  
  public static String joinStrings(String[] stringList, int startIndex) { return joinStrings(stringList, startIndex, stringList.length); }

  
  private static String joinStrings(String[] stringList, int startIndex, int endIndex) {
    if (startIndex < stringList.length) {
      StringBuilder stringBuilder = new StringBuilder(stringList[startIndex]);
      endIndex = Math.min(endIndex, stringList.length);
      for (int i = ++startIndex; i < endIndex; i++) {
        stringBuilder.append(" ").append(stringList[i]);
      }
      
      return stringBuilder.toString();
    } 
    
    return "";
  }
  
  public static int parseInt(String intString, int fallBack) {
    try {
      return Integer.parseInt(intString);
    } catch (NumberFormatException ex) {
      return fallBack;
    } 
  }
  
  public static long parseLong(String longString, int fallBack) {
    try {
      return Long.parseLong(longString);
    } catch (NumberFormatException ex) {
      return fallBack;
    } 
  }
  
  public static String concatStringList(String[] stringList) {
    StringJoiner joiner = new StringJoiner(" ");
    
    for (String string : stringList) {
      joiner.add(string);
    }
    
    return joiner.toString();
  }
  
  public static void broadcastWithRank(String message, RankType rankType) {
    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      Profile onlineUser = RedrockPlugin.getInstance().getProfileManager().getProfile(onlinePlayer.getUniqueId());
      RankType userRankType = onlineUser.getRankType();
      
      if (userRankType.isAboveOrEqual(rankType)) {
        onlinePlayer.sendMessage(message);
      }
    } 
  }
  
  public static long stringTimeReplacer(CommandSender sender, String durationString) {
    if (durationString.substring(0, 1).matches("[0-9]")) {
      
      try {
        char durationEndWith = durationString.charAt(durationString.length() - 1);
        long duration = Long.parseLong(durationString.substring(0, durationString.length() - 1));
        
        if (duration < 1L) return -30L; 
        if (durationEndWith == 's' || durationEndWith == 'S') {
          duration *= 1000L;
        } else if (durationEndWith == 'm' || durationEndWith == 'M') {
          duration = duration * 1000L * 60L;
        } else if (durationEndWith == 'h' || durationEndWith == 'H') {
          duration = duration * 1000L * 60L * 60L;
        } else if (durationEndWith == 'd' || durationEndWith == 'D') {
          duration = duration * 1000L * 60L * 60L * 24L;
        } else if (durationEndWith == 'w' || durationEndWith == 'W') {
          duration = duration * 1000L * 60L * 60L * 24L * 7L;
        } else if (durationEndWith == 'n' || durationEndWith == 'N') {
          duration = duration * 1000L * 60L * 60L * 24L * 30L;
        } else if (durationEndWith == 'y' || durationEndWith == 'Y') {
          duration = duration * 1000L * 60L * 60L * 24L * 365L;
        } else {
          sender.sendMessage(ChatColor.RED + "Geçersiz süre formatı, doğru kullanım: (#s/m/h/d/w/n)");
          return -30L;
        } 
        
        Profile basicUser = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        if (basicUser != null && !basicUser.getRankType().isAboveOrEqual(RankType.ADMIN) && duration > 31536000000L) {
          sender.sendMessage(ChatColor.RED + "En fazla 1 yıla kadar işlem uygulaya bilirsiniz.");
          return -30L;
        } 
        
        return duration;
      } catch (Exception ex) {
        sender.sendMessage(ChatColor.RED + "Geçersiz süre biçimi girildi: " + durationString);
        return -30L;
      } 
    }
    
    sender.sendMessage(ChatColor.RED + "Geçersiz süre biçimi girildi: " + durationString);
    return -30L;
  }
  
  public static long stringTimeReplacer(String durationString) {
    if (durationString.substring(0, 1).matches("[0-9]")) {
      
      try {
        char durationEndWith = durationString.charAt(durationString.length() - 1);
        long duration = Long.valueOf(durationString.substring(0, durationString.length() - 1)).longValue();
        
        if (duration < 1L) return -10L; 
        if (durationEndWith == 's' || durationEndWith == 'S') {
          duration *= 1000L;
        } else if (durationEndWith == 'm' || durationEndWith == 'M') {
          duration = duration * 1000L * 60L;
        } else if (durationEndWith == 'h' || durationEndWith == 'H') {
          duration = duration * 1000L * 60L * 60L;
        } else if (durationEndWith == 'd' || durationEndWith == 'D') {
          duration = duration * 1000L * 60L * 60L * 24L;
        } else if (durationEndWith == 'w' || durationEndWith == 'W') {
          duration = duration * 1000L * 60L * 60L * 24L * 7L;
        } else if (durationEndWith == 'n' || durationEndWith == 'N') {
          duration = duration * 1000L * 60L * 60L * 24L * 30L;
        } else if (durationEndWith == 'y' || durationEndWith == 'Y') {
          duration = duration * 1000L * 60L * 60L * 24L * 365L;
        } else {
          return -30L;
        } 
        
        return duration;
      } catch (Exception ex) {
        return -30L;
      } 
    }
    
    return -30L;
  }
  
  public static List<String> addLineBreaks(String message, int maxLineLength) {
    List<String> lineList = new ArrayList<>();
    StringBuilder stringBuilder = new StringBuilder();
    
    String[] words = message.split(" ");
    for (String word : words) {
      if (stringBuilder.length() != 0) stringBuilder.append(" "); 
      stringBuilder.append(word);
      
      if (stringBuilder.toString().length() > maxLineLength) {
        lineList.add(ChatColor.WHITE + stringBuilder.toString());
        stringBuilder = new StringBuilder();
      } 
    } 
    
    lineList.add(ChatColor.WHITE + stringBuilder.toString());
    return lineList;
  }

  
  public static String addNameAdditional(String targetName, boolean addSeparator) { return addNameAdditional(targetName, addSeparator, ChatColor.RESET); }

  
  public static String addNameAdditional(String targetName, boolean addSeparator, ChatColor chatColor) {
    String vowels = "aeıioöuü";
    StringBuilder builder = new StringBuilder();
    char lastCharacter = 'a';
    
    if (addSeparator) builder.append("'"); 
    for (int i = targetName.length() - 1; i > 0; i--) {
      if (vowels.contains(targetName.charAt(i) + "")) {
        lastCharacter = targetName.charAt(i);
        if (i == targetName.length() - 1) builder.append("n");
        
        break;
      } 
    } 
    if (lastCharacter == 'a' || lastCharacter == 'ı') builder.append("ın");
    if (lastCharacter == 'e' || lastCharacter == 'i') builder.append("in"); 
    if (lastCharacter == 'o' || lastCharacter == 'u') builder.append("un"); 
    if (lastCharacter == 'ö' || lastCharacter == 'ü') builder.append("ün");
    targetName = targetName + chatColor.toString() + builder.toString();
    return targetName;
  }

  
  public static boolean isIntensiveEntity(Entity entity) { return (entity instanceof org.bukkit.entity.Item || entity instanceof org.bukkit.entity.TNTPrimed || entity instanceof org.bukkit.entity.ExperienceOrb || entity instanceof org.bukkit.entity.FallingSand || (entity instanceof org.bukkit.entity.LivingEntity && !(entity instanceof org.bukkit.entity.Tameable) && !(entity instanceof Player))); }







  
  public static boolean isInt(String sInt) {
    try {
      Integer.parseInt(sInt);
    } catch (NumberFormatException ex) {
      return false;
    } 
    
    return true;
  }
}
