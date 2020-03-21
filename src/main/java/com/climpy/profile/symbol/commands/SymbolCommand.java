package com.climpy.profile.symbol.commands;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.symbol.SymbolMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SymbolCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
      Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
      if (sender instanceof Player && !user.getRankType().isAboveOrEqual(RankType.VIP)) {
      sender.sendMessage(ChatColor.RED + "Sembol menüsüne erişmek için " + RankType.VIP.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
      return true;
      }

      if (sender instanceof Player) {
         Player player = Bukkit.getPlayer(user.getUniqueUUID());
         SymbolMenu.INVENTORY.open(player);
       //  player.sendMessage(C.color("&7Merhaba, " + user.getRankType().getColor() + sender.getName() + "&7! Sembol menüsü hala geliştirilme aşamasındadır, menü sizin için açıldı ancak menüde oluşabilecek hataları tespit etmemizi kolaylaştırmak adına bize &c/reportbug [Sorun içeriği..] &7ile bildirmeniz isteğimizdir."));
         return true;
      } sender.sendMessage("Oyuncu dglsn uza.");
      return true;
      }
}