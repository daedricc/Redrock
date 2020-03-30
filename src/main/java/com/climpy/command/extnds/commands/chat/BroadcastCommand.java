package com.climpy.command.extnds.commands.chat;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.utils.C;
import com.climpy.utils.MiscUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BroadcastCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        Player player = Bukkit.getPlayer(sender.getName());
        if (sender instanceof Player) {
            if (!user.toPlayer().hasPermission("redrock.mod")) {
                sender.sendMessage(Locale.format(Locale.NO_PERM));
                return true;
            }
        }

        if (args.length < 1) {
            sender.sendMessage(C.RED + "Kullanım: /" + label + " [bilgilendirme-mesajı..]");
            return true;
        }

        String broadcastMessage = MiscUtils.joinStrings(args, 0);
        Bukkit.broadcastMessage(C.translate("&6[Duyuru] &r" + broadcastMessage));
        return true;
    }
}
