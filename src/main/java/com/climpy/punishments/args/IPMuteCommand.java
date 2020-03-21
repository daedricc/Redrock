package com.climpy.punishments.args;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.ProfileManager;

import com.climpy.profile.rank.RankType;
import com.climpy.punishments.Punishment;
import com.climpy.punishments.PunishmentManager;
import com.climpy.punishments.PunishmentType;
import com.climpy.punishments.extnds.Mute;
import com.climpy.utils.MiscUtils;
import com.climpy.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class IPMuteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
        if (!(sender instanceof ConsoleCommandSender)) {
            Profile profile = profileManager.getProfile(sender.getName());
            if (!profile.getRankType().isAboveOrEqual(RankType.MOD)) {
                sender.sendMessage(ChatColor.RED + "Geçersiz izin parametresi.");
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
        Profile targetUser = profileManager.getProfile(offlinePlayer.getUniqueId());
        if (targetUser == null) {
            sender.sendMessage(Locale.format(Locale.PLAYER_NOT_FOUND));
            return true;
        }

        long duration = TimeUtils.getTimeParser(silent ? args.length <= 2 ? "0" : args[2] : args.length <= 1 ? "0" : args[1]);
        String reason = MiscUtils.joinStrings(args, silent ? duration == 0 ? 2 : 3 : duration == 0 ? 1 : 2);
        if (reason.isEmpty()) reason = "Haksız Avantaj";

        UUID uniqueUUID = null;
        if (sender instanceof Player) {
            uniqueUUID = ((Player) sender).getUniqueId();
        }

        PunishmentManager punishmentManager = RedrockPlugin.getInstance().getPunishmentManager();
        Punishment punishment = punishmentManager.getPunishment(targetUser.getUniqueUUID(), PunishmentType.MUTE, PunishmentType.IP_MUTE);
        if (punishment != null) {
            sender.sendMessage(ChatColor.RED + "Kullanıcı zaten alternatif ceza kapsamında.");
            return true;
        }

        punishment = duration == 0 ? new Mute(PunishmentType.IP_MUTE, targetUser.getCurrentAddress(), targetUser.getUniqueUUID(), uniqueUUID, reason) :
                new Mute(PunishmentType.IP_MUTE, targetUser.getCurrentAddress(), targetUser.getUniqueUUID(), uniqueUUID, reason, System.currentTimeMillis() + duration);

        punishmentManager.applyPunishment(punishment, silent, true);
        return true;
    }
}
