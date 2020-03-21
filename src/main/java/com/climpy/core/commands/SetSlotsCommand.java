package com.climpy.core.commands;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.utils.BukkitReflection;
import com.climpy.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSlotsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        Player player = Bukkit.getPlayer(sender.getName());
        if (sender instanceof Player) {
            if (!user.getRankType().isAboveOrEqual(RankType.MOD)) {
                sender.sendMessage(C.RED + "Bu işlem için " + RankType.MOD.getDisplayName() + C.RED + " ve üzeri yetki gereklidir.");
                return true;
            }
        }

        if (args.length < 1) {
            player.sendMessage(C.RED + "Kullanım: /" + label + " <slot-Sayısı>");
            return true;
        }

        int slots = Integer.parseInt(args[0]);
        if (slots < 0) {
            player.sendMessage(C.RED + "'" + slots + "' geçerli bir sayı değil.");
            return true;
        }

        BukkitReflection.setMaxPlayers(RedrockPlugin.getInstance().getServer(), slots);
        Bukkit.broadcastMessage(C.RED + "[⚠] " + user.getRankType().getColor() + player.getName() + C.GOLD + " maksimum oyuncu sayısını " + C.WHITE + slots + C.GOLD + " olarak değiştirdi.");
        return true;
    }
}
