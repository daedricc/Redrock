package com.climpy.punishments.args;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.punishments.Punishment;
import com.climpy.punishments.PunishmentManager;
import com.climpy.punishments.PunishmentType;
import com.climpy.punishments.extnds.Ban;
import com.climpy.utils.DateTimeUtils;
import com.climpy.utils.MiscUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class BanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Profile profile = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        if (!(sender instanceof ConsoleCommandSender)) {
            if(!profile.toPlayer().hasPermission("redrock.moderator")) {
            sender.sendMessage(Locale.format(Locale.NO_PERM));
            return true;
        }
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Kullanım: /" + label + " (" + ChatColor.YELLOW + "-s" + ChatColor.RED + ") <oyuncu> <süre> [sebep...]");
            sender.sendMessage(ChatColor.RED + "Örnek: /" + label + " -s Red 7d (MCheat) Haksız Avantaj");
            return true;
        }

        boolean silent = false;
        if (args[0].equals("-s")) {
            silent = true;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(silent ? args[1] : args[0]);
        Profile targetProfile = RedrockPlugin.getInstance().getProfileManager().getProfile(offlinePlayer.getUniqueId());
        if (targetProfile == null) {
            sender.sendMessage(String.format(ChatColor.RED + "%1$s adlı oyuncu bulunamadı.", silent ? args[1] : args[0]));
            return true;
        }

        long duration = DateTimeUtils.parseLong(silent ? args.length <= 2 ? "0" : args[2] : args.length <= 1 ? "0" : args[1]);
        String reason = MiscUtils.joinStrings(args, silent ? duration == 0 ? 2 : 3 : duration == 0 ? 1 : 2);
        if (reason.isEmpty()) reason = "[MoosyAC] Haksız Avantaj";

        PunishmentManager punishmentManager = RedrockPlugin.getInstance().getPunishmentManager();
        Punishment punishment = punishmentManager.getPunishment(targetProfile.getUniqueUUID(), PunishmentType.BAN, PunishmentType.IP_BAN, PunishmentType.BLACKLIST);
        if (punishment != null) {
            sender.sendMessage(ChatColor.RED + "Kullanıcı zaten alternatif ceza kapsamında.");
            return true;
        }

        punishment = duration == 0 ? new Ban(PunishmentType.BAN, targetProfile.getCurrentAddress(), targetProfile.getUniqueUUID(), profile.getUniqueUUID(), reason) :
                new Ban(PunishmentType.BAN, targetProfile.getCurrentAddress(), targetProfile.getUniqueUUID(), profile.getUniqueUUID(), reason, System.currentTimeMillis() + duration);

        punishmentManager.applyPunishment(punishment, silent, true);
        return true;
    }
}
