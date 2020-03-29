package com.climpy.command.commands.core;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.utils.C;
import com.climpy.utils.MiscUtils;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AltsCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command label, String s, String[] args) {
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        if (sender instanceof Player) {
            if (!user.getRankType().isAboveOrEqual(RankType.MOD)) {
                sender.sendMessage(Locale.format(Locale.NO_PERM));
                return true;
            }
        }

        if (args.length < 1) {
            sender.sendMessage(C.RED + "Kullanım: /" + label.getName() + " <kullanıcıAdı>");
            return true;
        }

        Profile targetProfile = RedrockPlugin.getInstance().getProfileManager().getProfile(args[0]);
        if (targetProfile == null) {
            sender.sendMessage(Locale.format(Locale.PLAYER_NOT_FOUND));
            return true;
        }

        StringBuilder alts = new StringBuilder();
        for (UUID altUuid : targetProfile.getAlts()) {
            Profile altProfile = RedrockPlugin.getInstance().getProfileManager().getProfile(altUuid);

            if (altProfile != null) {
                alts.append(altProfile.getName()).append(", ");
            }
        }

        sender.sendMessage(C.CHAT_BAR);
        sender.sendMessage(C.YELLOW + "IP: " + targetProfile.getCurrentAddress());
        sender.sendMessage(C.YELLOW + "Alternatif Hesaplar: " + C.WHITE + (targetProfile.getAlts().isEmpty() ? "(0)" : "(" + targetProfile.getAlts().size() + ")"));
        sender.sendMessage(C.GOLD + " » " + C.RED + MiscUtils.separatorCheck(alts.toString(), ", "));
        sender.sendMessage(C.CHAT_BAR);
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        Validate.notNull(sender, "Sender boş olamaz");
        Validate.notNull(args, "Arguments boş olamaz");
        Validate.notNull(label, "Alias boş olamaz");
        if (args.length == 1) {
            List<String> onlinePlayers = new ArrayList<>();
            for (Profile user : RedrockPlugin.getInstance().getProfileManager().getDatabaseProfiles()) {
                if (user.getName().toLowerCase().startsWith(args[0].toLowerCase()))
                    onlinePlayers.add(user.getName());
            }
            return onlinePlayers;
        }
        return ImmutableList.of();
    }
}
