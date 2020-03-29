package com.climpy.punishments.history;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.punishments.Punishment;
import com.climpy.punishments.PunishmentManager;
import com.climpy.punishments.history.menu.HistoryCategoryMenu;
import com.climpy.redis.data.DataPublisher;
import com.climpy.utils.C;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class HistoryCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        Profile profile = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        if (!profile.getRankType().isAboveOrEqual(RankType.MOD)) {
            sender.sendMessage(Locale.format(Locale.NO_PERM));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(C.RED + "Kullanım: /" + label + " <oyuncuAdı>");
            return true;
        }

        switch(args[0]) {
            case "clear": {
                if (args.length < 2) {
                    sender.sendMessage(C.RED + "Kullanım: /" + label + " clear <oyuncuAdı>");
                    return true;
                }

                Profile targetProfile = RedrockPlugin.getInstance().getProfileManager().getProfile(args[1]);
                if (targetProfile == null) {
                    sender.sendMessage(Locale.format(Locale.PLAYER_NOT_FOUND));
                    return true;
                }

                List<Punishment> punishments = targetProfile.getPunishments();
                if (punishments.isEmpty()) {
                    sender.sendMessage(C.RED + "Bu kullanıcının ceza geçmişi bulunamadı.");
                    return true;
                }

                DataPublisher dataPublisher = RedrockPlugin.getInstance().getRedisConnection().getDataPublisher();
                JsonObject punishmentClearObject = new JsonObject();
                punishmentClearObject.addProperty("uniqueUUID", targetProfile.getUniqueUUID().toString());
                dataPublisher.write("punishment_clear", punishmentClearObject);

                sender.sendMessage(targetProfile.getRankType().getColor() + targetProfile.getName() + C.GREEN + " adlı oyuncunun ceza geçmişi temizlendi.");
                break;
            }

            case "fetch": {
                PunishmentManager punishmentManager = RedrockPlugin.getInstance().getPunishmentManager();
                for (UUID uuid : punishmentManager.getDatabasePunishments().keySet()) {
                   Profile profile1 = RedrockPlugin.getInstance().getProfileManager().getProfile(uuid);
                   if(profile1 == null) {
                       continue;
                   }

                    DataPublisher dataPublisher = RedrockPlugin.getInstance().getRedisConnection().getDataPublisher();
                    JsonObject punishmentClearObject = new JsonObject();
                    punishmentClearObject.addProperty("uniqueUUID", profile1.getUniqueUUID().toString());
                    dataPublisher.write("punishment_clear", punishmentClearObject);
                }

                sender.sendMessage(C.GREEN + "Veritabanındaki tüm cezalar temizlendi.");
                break;
            }

            default: {
                Profile targetProfile = RedrockPlugin.getInstance().getProfileManager().getProfile(args[0]);
                if (targetProfile == null) {
                    sender.sendMessage(Locale.format(Locale.PLAYER_NOT_FOUND));
                    return true;
                }

                List<Punishment> punishments = targetProfile.getPunishments();
                if (punishments.isEmpty()) {
                    sender.sendMessage(C.RED + "Bu kullanıcının ceza geçmişi bulunamadı.");
                    return true;
                }

                HistoryCategoryMenu.getInventory(targetProfile).open(player);
            }
        }
        return true;
    }
}
