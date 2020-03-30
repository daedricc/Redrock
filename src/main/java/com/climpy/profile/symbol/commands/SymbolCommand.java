package com.climpy.profile.symbol.commands;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.symbol.SymbolMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SymbolCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        if (!user.toPlayer().hasPermission("redrock.vip")) {
            sender.sendMessage(Locale.format(Locale.NO_PERM));
            return true;
        }

        if (sender instanceof Player) {
            Player player = Bukkit.getPlayer(user.getUniqueUUID());
            SymbolMenu.INVENTORY.open(player);
            //  player.sendMessage(C.color("&7Merhaba, " + user.getRankType().getColor() + sender.getName() + "&7! Sembol menüsü hala geliştirilme aşamasındadır, menü sizin için açıldı ancak menüde oluşabilecek hataları tespit etmemizi kolaylaştırmak adına bize &c/reportbug [Sorun içeriği..] &7ile bildirmeniz isteğimizdir."));
            return true;
        }
        sender.sendMessage("Oyuncu dglsn uza.");
        return true;
    }
}