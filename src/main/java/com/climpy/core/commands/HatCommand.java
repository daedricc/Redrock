package com.climpy.core.commands;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.utils.C;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;

public class HatCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
      Player player = Bukkit.getPlayer(sender.getName());
      if (sender instanceof Player) {
          Player targetPlayer;
          Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());
          if (!user.getRankType().isAboveOrEqual(RankType.MOD)) {
              sender.sendMessage(C.RED + "Bu işlem için " + RankType.MOD.getColor() + RankType.MOD.getDisplayName() + C.RED + " ve üzeri yetki gereklidir.");
              return true;
          }

          ItemStack playerHandItem = player.getItemInHand();
          if (args.length == 1) {
              targetPlayer = Bukkit.getPlayerExact(args[0]);
              if (targetPlayer == null) {
                  sender.sendMessage(Locale.format(Locale.PLAYER_OFFLINE));
              }
          } else {
              targetPlayer = player;
          }

          PlayerInventory targetInventory = targetPlayer.getInventory();
          if (playerHandItem.getType() == null || playerHandItem.getType() == Material.AIR) {
              sender.sendMessage(C.RED + "Elinizde bir item olmalı.");
              return true;
          }

          if (playerHandItem.getType() != null && playerHandItem.getType().getMaxDurability() != 0) {
              sender.sendMessage(C.RED + "Elinizde olan eşya şapka olarak giyilmez.");
              return true;
          }

          if (targetInventory.getHelmet() != null) {
              sender.sendMessage(C.RED + targetPlayer.getName() + " zaten şu anda bir şapka takmış durumda.");
              return true;
          }

          ItemStack itemHand = playerHandItem;
          ItemStack itemRemove = new ItemStack(Material.AIR);
          targetInventory.setItemInHand(itemRemove);
          targetInventory.setHelmet(itemHand);

          if (targetPlayer == sender) {
              sender.sendMessage(C.GREEN + "Şapka ayarlandı: " + C.WHITE + playerHandItem
                      .getType().name());
          } else {
              sender.sendMessage(C.GREEN + targetPlayer.getName() + " için bir şapka ayarladınız.");
              targetPlayer.sendMessage(C.GREEN + "Şapka ayarlandı: " + C.WHITE + playerHandItem
                      .getType().name());
          }
          return true;
      }
      sender.sendMessage("Bu islem sadece oyundan yurutelebilir.");
      return true;
  }


    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
            Validate.notNull(sender, "Sender bos olamaz");
            Validate.notNull(args, "Arguments bos olamaz");
            Validate.notNull(label, "Alias bos olamaz");
            
            List<String> onlinePlayers = new ArrayList<>();
            
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.canSee(player) && player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                    onlinePlayers.add(player.getName());
                }
            } 
            
            return onlinePlayers;
        }
}
