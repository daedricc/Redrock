package com.climpy.commands.commands;

import com.climpy.Locale;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class GameModeCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        if (sender instanceof Player) {
            Player targetPlayer;
            User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getUniqueId());
            if (!user.getRankType().isAboveOrEqual(RankType.MOD)) {
                sender.sendMessage(ChatColor.RED + "Bu işlem için " + RankType.MOD.getColor() + RankType.MOD.getDisplayName() + ChatColor.RED + " ve üzeri rütbe gereklidir.");
                return true;
            }

            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "Kullanım: /" + label + " <oyun-Modu> <oyuncuAdı>");
                return true;
            }

            GameMode gameMode = gameModeName(args[0]);
            if (gameMode == null) {
                sender.sendMessage(ChatColor.RED + "Girilen oyun modu '" + args[0] + "' geçersiz.");
                return true;
            }

            if (args.length == 2) {
                if (user.getRankType().isAboveOrEqual(RankType.ADMIN)) {
                    targetPlayer = Bukkit.getPlayerExact(args[1]);
                    if (targetPlayer == null) {
                        sender.sendMessage(Locale.format(Locale.PLAYER_NOT_FOUND));
                        return true;
                    }
                } else {
                    targetPlayer = (Player) sender;
                }
            } else {
                targetPlayer = (Player) sender;
            }

            if (targetPlayer.getGameMode() == gameMode) {
                sender.sendMessage(ChatColor.RED + "Oyun modu " + targetPlayer.getName() + " için zaten " + ChatColor.WHITE + gameMode.name() + ChatColor.RED + '.');
                return true;
            }


            targetPlayer.setGameMode(gameMode);
            if (targetPlayer == sender) {
                sender.sendMessage(ChatColor.GOLD + "Oyun modu sizin için " + ChatColor.WHITE + gameMode.name() + ChatColor.GOLD + " olarak ayarlandı.");
                return true;
            }
            sender.sendMessage(ChatColor.GOLD + "Oyun modu " + targetPlayer.getName() + ChatColor.GOLD + " için " + ChatColor.WHITE + gameMode.name() + ChatColor.GOLD + " olarak ayarlandı.");
            return true;
        }

        sender.sendMessage("Bu islem sadece oyundan yurutulebilir.");
        return true;
    }


    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        Validate.notNull(sender, "Sender bos olamaz");
        Validate.notNull(args, "Arguments bos olamaz");
        Validate.notNull(label, "Alias bos olamaz");

        if (args.length == 2) {
            List<String> onlinePlayers = new ArrayList<>();

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.canSee(player) && player.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                    onlinePlayers.add(player.getName());
                }
            }

            return onlinePlayers;
        }

        return null;
    }

    private GameMode gameModeName(String gameModeName) {
        if (gameModeName.toLowerCase().equals("a") || gameModeName.toLowerCase().equals("advanture") || gameModeName.toLowerCase().equals("2") || gameModeName.toLowerCase().equals("advan"))
            return GameMode.ADVENTURE;
        if (gameModeName.toLowerCase().equals("c") || gameModeName.toLowerCase().equals("creative") || gameModeName.toLowerCase().equals("1") || gameModeName.toLowerCase().equals("creat"))
            return GameMode.CREATIVE;
        if (gameModeName.toLowerCase().equals("s") || gameModeName.toLowerCase().equals("survival") || gameModeName.toLowerCase().equals("0") || gameModeName.toLowerCase().equals("surv"))
            return GameMode.SURVIVAL;
        if (gameModeName.toLowerCase().equals("spec") || gameModeName.toLowerCase().equals("spectator") || gameModeName.toLowerCase().equals("3") || gameModeName.toLowerCase().equals("sp")) {
            return GameMode.SPECTATOR;
        }
        return null;
    }
}
