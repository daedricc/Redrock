package com.climpy.punishments.args;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.ProfileManager;
import com.climpy.profile.rank.RankType;
import com.climpy.punishments.Punishment;
import com.climpy.punishments.PunishmentManager;
import com.climpy.punishments.extnds.Warn;
import com.climpy.utils.MiscUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class WarnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
        if (!(sender instanceof ConsoleCommandSender)) {
            Profile profile = profileManager.getProfile(sender.getName());
            if (!profile.getRankType().isAboveOrEqual(RankType.MOD)) {
                sender.sendMessage(Locale.format(Locale.NO_PERM));
                return true;
            }
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Kullanım: /" + label + " (" + ChatColor.YELLOW + "-s" + ChatColor.RED + ") <oyuncu> [sebep...]");
            sender.sendMessage(ChatColor.RED + "Örnek: /" + label + " -s Red (MCheat) Haksız Avantaj");
            return true;
        }

        boolean silent = false;
        if (args[0].equals("-s")) {
            silent = true;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(silent ? args[1] : args[0]);
        Profile targetUser = profileManager.getProfile(offlinePlayer.getUniqueId());
        if (targetUser == null) {
            sender.sendMessage(Locale.format(Locale.PLAYER_NOT_FOUND));
            return true;
        }

        String reason = MiscUtils.joinStrings(args, silent ? 2 : 1);
        if (reason.isEmpty()) reason = "Haksız Avantaj";

        UUID uniqueUUID = null;
        if (sender instanceof Player) {
            uniqueUUID = ((Player) sender).getUniqueId();
        }

        PunishmentManager punishmentManager = RedrockPlugin.getInstance().getPunishmentManager();
        Punishment punishment = new Warn(targetUser.getCurrentAddress(), targetUser.getUniqueUUID(), uniqueUUID, reason);

        punishmentManager.applyPunishment(punishment, silent, true);
        return true;
    }
}
