package com.climpy.report.menu.commands;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.report.ReportData;
import com.climpy.report.ReportManager;
import com.climpy.report.menu.ReportProfileMenu;
import com.climpy.report.menu.ReportsMenu;
import com.climpy.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ReportsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command label, String s, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        Profile profile = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        if (!profile.getRankType().isAboveOrEqual(RankType.MOD)) {
            sender.sendMessage(Locale.format(Locale.NO_PERM));
            return true;
        }

        if (args.length < 1) {
            ReportManager reportManager = RedrockPlugin.getInstance().getReportManager();

            List<ReportData> reportManagers = reportManager.getDatabaseReports();
            if (reportManagers.isEmpty()) {
                sender.sendMessage(C.RED + "Sunucuda rapor geçmişi bulunamadı.");
                return true;
            }

            ReportsMenu.getInventory(reportManager).open(player);
            return true;
        }

        Profile targetProfile = RedrockPlugin.getInstance().getProfileManager().getProfile(args[0]);
        if (targetProfile == null) {
            sender.sendMessage(Locale.format(Locale.PLAYER_NOT_FOUND));
            return true;
        }

        ReportManager reportManager = RedrockPlugin.getInstance().getReportManager();

        List<ReportData> reportData = reportManager.getDatabaseReports();
        if (reportData.isEmpty()) {
            sender.sendMessage(C.RED + "Bu kullanıcının rapor geçmişi bulunamadı.");
            return true;
        }

        ReportProfileMenu.getInventory(reportManager, targetProfile).open(player);
        return true;
    }
}
