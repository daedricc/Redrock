package com.climpy.profile.symbol.commands;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.symbol.SymbolType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSymbolCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        Player player = (Player) sender;

        if (sender instanceof Player &&!user.toPlayer().hasPermission("redrock.admin")) {
            sender.sendMessage(Locale.format(Locale.NO_PERM));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDoğru Kullanım: /" + label + " [Oyuncu-Adı] <symbolID>"));
            return true;
        }

        Profile targetUser = RedrockPlugin.getInstance().getProfileManager().getProfile(args[0]);

        if (targetUser == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f" + args[0] + " &cismine sahip bir oyuncu bulunamadı."));
            return true;
        }

        SymbolType symbolType;

        try {
            symbolType = SymbolType.valueOf(args[1].toUpperCase(java.util.Locale.ENGLISH));
        } catch (Exception e) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cGirilen &f" + args[1] + " &csembolü bulunamadı. "));
            return true;
        }

        Player t = Bukkit.getPlayer(args[0]);

        targetUser.setSymbolType(symbolType);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', user.getActiveRank().getColor() + t.getName() + " &aoyuncusunun sembolü &f" + symbolType.getDisplayName() + " &aolarak güncellendi."));
        t.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSembolünüz &f" + symbolType.getDisplayName() + " &aolarak güncellendi."));
        targetUser.saveProfile();
        return true;
    }
}
