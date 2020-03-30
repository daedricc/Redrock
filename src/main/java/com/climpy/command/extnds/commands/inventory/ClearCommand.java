package com.climpy.command.extnds.commands.inventory;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.server.ServerType;
import com.climpy.server.provider.DefaultServerProvider;
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
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        DefaultServerProvider defaultServerProvider = new DefaultServerProvider();

        if (defaultServerProvider.getServerType() == ServerType.LOBI) {
            sender.sendMessage(C.RED + "Lobi sunucularında bu komutu kullanamazsınız.");
            return true;
        }

        if (sender instanceof Player) {
            if(!user.toPlayer().hasPermission("redrock.moderator")) {
            sender.sendMessage(Locale.format(Locale.NO_PERM));
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
                Profile targetUser = RedrockPlugin.getInstance().getProfileManager().getProfile(args[0]);
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