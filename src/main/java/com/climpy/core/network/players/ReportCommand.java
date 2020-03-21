package com.climpy.core.network.players;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.methods.RedrockPacket;
import com.climpy.profile.Profile;
import com.climpy.redis.data.DataPublisher;
import com.climpy.report.ReportData;
import com.climpy.server.provider.DefaultServerProvider;
import com.climpy.utils.C;
import com.climpy.utils.CooldownUtils;
import com.climpy.utils.MiscUtils;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ReportCommand implements CommandExecutor {

    public ReportCommand() {
        CooldownUtils.createCooldown("Report");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        if (sender instanceof Player) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Kullanım: /" + label + " <oyuncuAdı> [sebep..]");
                return true;
            }

        if (CooldownUtils.isOnCooldown("Report", (Player) sender)) {
            sender.sendMessage(C.translate("&d&l" + CooldownUtils.getCooldownForPlayerInt("Report", (Player) sender)) + " &dsaniye sonra report gönderebilirsiniz.");
            return true;
        }

            Player targetPlayer = Bukkit.getPlayerExact(args[0]);
            if (targetPlayer == null) {
                sender.sendMessage(Locale.format(Locale.PLAYER_NOT_FOUND));
                return true;
            }

            if(targetPlayer == player) {
                player.sendMessage(C.RED + "Kendini şikayet edemezsin.");
                return true;
            }

            Profile reporterUser = RedrockPlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());
            Profile reportedUser = RedrockPlugin.getInstance().getProfileManager().getProfile(targetPlayer.getUniqueId());

            DefaultServerProvider defaultServerProvider = new DefaultServerProvider();
            String message = MiscUtils.joinStrings(args, 1);

            ReportData reportData = new ReportData(reportedUser.getUniqueUUID(), reporterUser.getUniqueUUID(), message, defaultServerProvider.getServerName());
            reportData.saveReport();

            sender.sendMessage(ChatColor.GREEN + "Raporunuz başarıyla sunucudaki tüm personellere gönderildi!");
            CooldownUtils.addCooldown("Report", player, 60);

            RedrockPacket.sendReportObject(UUID.fromString(reportData.getUniqueUUID().toString()), UUID.fromString(reportData.getAddedBy().toString()), reportData.getServer(), reportData.getReason());
            return true;
        }
        sender.sendMessage("Bu sadece oyundan yürütülür.");
        return true;
    }
}
