package com.climpy.redrock.report.menu.commands;

import com.climpy.redrock.Locale;
import com.climpy.redrock.RedrockPlugin;
import com.climpy.redrock.commands.RedrockCommand;
import com.climpy.redrock.commands.RedrockResultCommand;
import com.climpy.redrock.report.ReportData;
import com.climpy.redrock.report.ReportManager;
import com.climpy.redrock.report.menu.ReportProfileMenu;
import com.climpy.redrock.report.menu.ReportsMenu;
import com.climpy.torch.TorchPlugin;
import com.climpy.torch.profile.Profile;
import com.climpy.redrock.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ReportsCommand extends RedrockCommand {
    public ReportsCommand() {
        super("reports", "Report a player");
    }

    @Override
    public RedrockResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        Profile profile = TorchPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        if (!profile.getRank().hasPermission("torch.staff")) {
            sender.sendMessage(Locale.format(Locale.NO_PERM));
            return RedrockResultCommand.SUCCESS;
        }

        if (args.length < 1) {
            ReportManager reportManager = RedrockPlugin.getInstance().getReportManager();

            List<ReportData> reportManagers = reportManager.getDatabaseReports();
            if (reportManagers.isEmpty()) {
                sender.sendMessage(C.RED + "Sunucuda rapor geçmişi bulunamadı.");
                return RedrockResultCommand.SUCCESS;
            }

            ReportsMenu.getInventory(reportManager).open(player);
            return RedrockResultCommand.SUCCESS;
        }

        Profile targetProfile = TorchPlugin.getInstance().getProfileManager().getProfile(args[0]);
        if (targetProfile == null) {
            sender.sendMessage(Locale.format(Locale.PLAYER_NOT_FOUND));
            return RedrockResultCommand.SUCCESS;
        }

        ReportManager reportManager = RedrockPlugin.getInstance().getReportManager();

        List<ReportData> reportData = reportManager.getDatabaseReports();
        if (reportData.isEmpty()) {
            sender.sendMessage(C.RED + "Bu kullanıcının rapor geçmişi bulunamadı.");
            return RedrockResultCommand.SUCCESS;
        }

        ReportProfileMenu.getInventory(reportManager, targetProfile).open(player);
        return RedrockResultCommand.SUCCESS;
    }
}
