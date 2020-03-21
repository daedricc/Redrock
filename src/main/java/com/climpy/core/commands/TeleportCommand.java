package com.climpy.core.commands;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand implements CommandExecutor {
	public boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
		Player player = (Player) sender;

		if (sender instanceof Player && !user.getRankType().isAboveOrEqual(RankType.MOD)) {
			sender.sendMessage(C.RED + "Bu işlem için " + RankType.MOD.getDisplayName() + C.RED + " ve üzeri rütbe gereklidir.");
			return true;
		}

		if (args.length == 1) {
			Player p = (Player) sender;
			Player t = Bukkit.getPlayer(args[0]);
			if (t == null) {
				sender.sendMessage(Locale.format(Locale.PLAYER_OFFLINE));
				return true;
			}
			p.teleport(t);
			sender.sendMessage(C.WHITE + t.getName() + C.GOLD + " oyuncusuna ışınlandın.");
			return true;
		}

		if (args.length == 2) {
			if (!user.getRankType().isAboveOrEqual(RankType.MOD)) {
				sender.sendMessage(C.RED + "Yetkiye sahip değilsin.");
				return true;
			}

			Player p = Bukkit.getPlayer(args[0]);
			Player t = Bukkit.getPlayer(args[1]);
			if (p == null) {
				sender.sendMessage(Locale.format(Locale.PLAYER_OFFLINE));
				return true;
			}
			if (t == null) {
				sender.sendMessage(Locale.format(Locale.PLAYER_OFFLINE));
				return true;
			}
			p.teleport(t);
			sender.sendMessage(C.WHITE + p.getName() + C.GOLD + " oyuncusunu " + C.WHITE + t.getName() + C.GOLD + " oyuncusuna ışınladın.");
			return true;
		}

		sender.sendMessage(C.RED + "Kullanım: /" + commandLabel + " <oyuncu> <isteğe bağlı: 2. oyuncu>");
		
		if (cmd.getName().equalsIgnoreCase("teleportall")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(C.RED + "konsol agla krdsm mal misin");
				return true;
			}
			Player p = (Player) sender;
			if (!user.getRankType().isAboveOrEqual(RankType.MOD)) {
				sender.sendMessage(C.RED + "Bu işlem için " + RankType.MOD.getDisplayName() + C.RED + " ve üzeri rütbe gereklidir.");
				return true;
			}
			
			if (args.length > 0) {
				sender.sendMessage(C.RED + "/" + commandLabel);
				return true;
			}
			for (Player online : Bukkit.getOnlinePlayers()) {
				if (online != p) {
					online.teleport(p);
					online.sendMessage(C.WHITE + p.getName() + C.GOLD + " isimli oyuncu herkesi kendine ışınlandı.");
				}
			}
			p.sendMessage(C.GREEN + "Tüm çevrim içi oyuncular şu anda size ışınlandı.");
			return true;
		}
		
		
		if (cmd.getName().equalsIgnoreCase("tphere")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(C.RED + "konsol agla krdsm mal misin");
				return true;
			}
			if (!user.getRankType().isAboveOrEqual(RankType.MOD)) {
				sender.sendMessage(C.RED + "Bu işlem için " + RankType.MOD.getDisplayName() + C.RED + " ve üzeri rütbe gereklidir.");
				return true;
			}
			
			if (args.length == 1) {
				Player p = (Player) sender;
				Player t = Bukkit.getPlayer(args[0]);
				if (t == null) {
					sender.sendMessage(Locale.format(Locale.PLAYER_OFFLINE));
					return true;
				}
				t.teleport(p);
				p.sendMessage(C.WHITE + t.getName() + C.GOLD + " oyuncusunu kendinize ışınlandınız.");
				t.sendMessage(C.WHITE + p.getName() + C.GOLD + " isimli oyuncu kendisini size ışınladı.");
				return true;
			}
				sender.sendMessage(C.RED + "/" + commandLabel + " <oyuncu>");
				return true;
		}
		
		
		if (cmd.getName().equalsIgnoreCase("tppos")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(C.RED + "konsol agla krdsm mal misin");
				return true;
			}
			if (!user.getRankType().isAboveOrEqual(RankType.MOD)) {
				sender.sendMessage(C.RED + "Bu işlem için " + RankType.MOD.getDisplayName() + C.RED + " ve üzeri rütbe gereklidir.");
				return true;
			}

			Player p = (Player) sender;
			
			if (args.length != 3) {
				sender.sendMessage(C.RED + "/" + commandLabel + " [x] [y] [z]");
				return true;
			}
			if (!isDouble(args[0]) || !isDouble(args[1]) || !isDouble(args[2])) {
				sender.sendMessage(C.RED + "/" + commandLabel + " [x] [y] [z]");
				return true;
			}
			double x = Double.parseDouble(args[0])+0.5;
			double y = Double.parseDouble(args[1])+0.5;
			double z = Double.parseDouble(args[2])+0.5;
			Location loc = new Location(p.getWorld(), x, y, z, p.getLocation().getYaw(), p.getLocation().getPitch());
			p.teleport(loc);
			p.sendMessage(C.GREEN + "Belirtilen kordinatlara ışınlandınız. §7(§a" + x + "§d, §a" + y + "§d, §a" + z + "§7)");
				return true;
		}
		return true;
	}
}
