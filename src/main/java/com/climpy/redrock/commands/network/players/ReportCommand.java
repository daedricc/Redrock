package com.climpy.redrock.commands.network.players;

import com.climpy.redrock.Locale;
import com.climpy.redrock.commands.RedrockCommand;
import com.climpy.redrock.commands.RedrockResultCommand;
import com.climpy.redrock.listener.methods.RedrockPacket;
import com.climpy.redrock.report.ReportData;
import com.climpy.redrock.server.provider.DefaultServerProvider;
import com.climpy.torch.TorchPlugin;
import com.climpy.torch.profile.Profile;
import com.climpy.redrock.utils.C;
import com.climpy.redrock.utils.CooldownUtils;
import com.climpy.redrock.utils.MiscUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ReportCommand extends RedrockCommand {

    public ReportCommand() {
        super("report");
        CooldownUtils.createCooldown("Report");
    }

    @Override
    public RedrockResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        if (sender instanceof Player) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Kullanım: /" + label + " <oyuncuAdı> [sebep..]");
                return RedrockResultCommand.SUCCESS;
            }

            if (CooldownUtils.isOnCooldown("Report", (Player) sender)) {
                sender.sendMessage(C.translate("&d&l" + CooldownUtils.getCooldownForPlayerInt("Report", (Player) sender)) + C.PINK + " saniye sonra report gönderebilirsiniz.");
                 return RedrockResultCommand.SUCCESS;
            }

            Player targetPlayer = Bukkit.getPlayerExact(args[0]);
            if (targetPlayer == null) {
                sender.sendMessage(Locale.format(Locale.PLAYER_NOT_FOUND));
                 return RedrockResultCommand.SUCCESS;
            }

            if (targetPlayer == player) {
                player.sendMessage(C.RED + "Kendini şikayet edemezsin.");
                 return RedrockResultCommand.SUCCESS;
            }

            Profile reporterUser = TorchPlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());
            Profile reportedUser = TorchPlugin.getInstance().getProfileManager().getProfile(targetPlayer.getUniqueId());

            DefaultServerProvider defaultServerProvider = new DefaultServerProvider();
            String message = MiscUtils.joinStrings(args, 1);

            ReportData reportData = new ReportData(reportedUser.getUniqueUUID(), reporterUser.getUniqueUUID(), message, defaultServerProvider.getServerName());
            reportData.saveReport();

            sender.sendMessage(ChatColor.GREEN + "Raporunuz başarıyla sunucudaki tüm personellere gönderildi!");
            CooldownUtils.addCooldown("Report", player, 60);

            RedrockPacket.sendReportObject(UUID.fromString(reportData.getUniqueUUID().toString()), UUID.fromString(reportData.getAddedBy().toString()), reportData.getServer(), reportData.getReason());
             return RedrockResultCommand.SUCCESS;
        }

        sender.sendMessage("Bu sadece oyundan yürütülür.");
         return RedrockResultCommand.SUCCESS;
    }
}
