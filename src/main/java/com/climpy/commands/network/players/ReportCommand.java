package com.climpy.commands.network.players;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.profile.utils.MiscUtils;
import com.climpy.redis.RedisManager;
import com.climpy.redis.data.DataPublisher;
import com.climpy.server.ServerData;
import com.climpy.server.provider.DefaultServerProvider;
import com.climpy.utils.C;
import com.climpy.utils.CooldownUtils;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

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

         //   if(targetPlayer == player) {
          //      player.sendMessage(C.RED + "nE, kendini şikayet edemezsin.");
          ///      return true;
           // }

            User reporterUser = ProfilePlugin.getInstance().getUserManager().getUser(player.getUniqueId());
            User reportedUser = ProfilePlugin.getInstance().getUserManager().getUser(targetPlayer.getUniqueId());

            DefaultServerProvider defaultServerProvider = new DefaultServerProvider();

            String message = MiscUtils.joinStrings(args, 1);

            if (!reporterUser.getRankType().isAboveOrEqual(RankType.CHAT_MOD))
            sender.sendMessage(ChatColor.GREEN + "Raporunuz başarıyla sunucudaki tüm personellere gönderildi!");
            CooldownUtils.addCooldown("Report", player, 60);
            DataPublisher dataPublisher = RedrockPlugin.getInstance().getRedisConnection().getDataPublisher();
            JsonObject reportObject = new JsonObject();
            reportObject.addProperty("reportedUUID", reportedUser.getUniqueUUID().toString());
            reportObject.addProperty("reporterUUID", reporterUser.getUniqueUUID().toString());
            reportObject.addProperty("serverName", defaultServerProvider.getServerName());
            reportObject.addProperty("message", message);
            dataPublisher.write("report_alerts", reportObject);
            return true;
        }
        sender.sendMessage("Bu sadece oyundan yürütülür.");
        return true;
    }
}
