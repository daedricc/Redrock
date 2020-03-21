package com.climpy.core.commands;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class HealCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
		Player p = Bukkit.getPlayer(sender.getName());

		if (sender instanceof Player && !p.isOp() && !user.getRankType().isAboveOrEqual(RankType.MVIP)) {
			sender.sendMessage(C.RED + "Bu işlem için " + RankType.MVIP.getDisplayName() + C.RED + " ve üzeri yetki gereklidir.");
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("heal")) {
			
			if (args.length == 0) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cKonsol bunu kullanamaz."));
					return true;
				}
				
				p.sendMessage("§aKendinizi iyileştirdiniz.");
				p.setHealth(20);
				p.setFireTicks(0);
				for (PotionEffect po : p.getActivePotionEffects()) {
					p.removePotionEffect(po.getType());
				}
				p.setFoodLevel(20);
				p.setSaturation(10);
				return true;
			}
			if (args.length > 1) {
				sender.sendMessage("§bDoğru Kullanım: §6/heal [oyuncu]");
				return true;
			}
			Player t = Bukkit.getPlayer(args[0]);
			if (t == null) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cOyuncu bulunamadı."));
				return true;
			}
			sender.sendMessage(user.getRankType().getColor() + t.getName() + " §aiyileştirildi.");
			t.sendMessage(user.getRankType().getColor() + sender.getName() + " §aadlı yönetici tarafından iyileştirildiniz.");
			t.setHealth(20);
			t.setFireTicks(0);
			for (PotionEffect to : t.getActivePotionEffects()) {
				t.removePotionEffect(to.getType());
			}
			t.setFoodLevel(20);
			t.setSaturation(10);
			return true;
		}
		return true;
	}
	
}
