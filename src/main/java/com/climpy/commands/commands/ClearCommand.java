package com.climpy.commands.commands;

import com.climpy.Locale;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class ClearCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());
        if (sender instanceof Player) {
            if (!user.getRankType().isAboveOrEqual(RankType.MOD)) {
                sender.sendMessage(C.RED + "Bu işlem için " + RankType.MOD.getColor() + RankType.MOD.getDisplayName() + C.RED + " ve üzeri yetki gereklidir.");
                return true;
            }

            if (args.length < 1) {
                PlayerInventory targetInventory = player.getInventory();
                targetInventory.setHelmet(null);
                targetInventory.setChestplate(null);
                targetInventory.setLeggings(null);
                targetInventory.setBoots(null);
                targetInventory.clear();
                sender.sendMessage(C.GREEN + "Envanteriniz silindi.");
            }


        if (args.length == 1) {
            User targetUser = ProfilePlugin.getInstance().getUserManager().getUser(args[0]);
            if (targetUser == null) {
                sender.sendMessage(Locale.format(Locale.PLAYER_NOT_FOUND));
                return true;
            }

            Player targetPlayer = Bukkit.getPlayer(targetUser.getUniqueUUID());
            if (targetPlayer == null) {
                sender.sendMessage(Locale.format(Locale.PLAYER_OFFLINE));
                return true;
            }

                PlayerInventory targetInventory = targetPlayer.getInventory();
                targetInventory.setHelmet(null);
                targetInventory.setChestplate(null);
                targetInventory.setLeggings(null);
                targetInventory.setBoots(null);
                targetInventory.clear();

                if (targetUser == user) {
                    sender.sendMessage(C.GREEN + "Envanteriniz silindi.");
                } else {
                    targetPlayer.sendMessage(C.GREEN + "Envanteriniz " + C.WHITE + sender.getName() + C.GREEN + " tarafından silindi.");
                    sender.sendMessage(C.GREEN + targetPlayer.getName() + " adlı oyuncunun envanteri silindi.");
                }
                return true;
            }
            return true;
        }
        return true;
    }
}