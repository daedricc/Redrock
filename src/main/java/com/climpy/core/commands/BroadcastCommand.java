package com.climpy.core.commands;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.utils.C;
import com.climpy.utils.StringUtil;
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
            if (!user.getRankType().isAboveOrEqual(RankType.CHAT_MOD)) {
                sender.sendMessage(C.RED + "Bu işlem için " + RankType.CHAT_MOD.getDisplayName() + C.RED + " ve üzeri yetki gereklidir.");
                return true;
            }
        }

        if (args.length < 1) {
            sender.sendMessage(C.RED + "Kullanım: /" + label + " [bilgilendirme-mesajı..]");
            return true;
        }

        String broadcastMessage = StringUtil.joinStrings(args, 0);
        Bukkit.broadcastMessage(C.translate("&6[Duyuru] &r" + broadcastMessage));
        return true;
    }
}
