package com.climpy.core.commands;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.utils.C;
import com.climpy.utils.NumberUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command label, String labelCmd, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        if (sender instanceof Player) {
            if (!player.isOp() && !user.getRankType().isAboveOrEqual(RankType.ADMIN)) {
                sender.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.MODPLUS.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
                return true;
            }
        }

        if(args.length < 3) {
            sender.sendMessage(C.translate("&4Kullanım: /" + labelCmd + " <ayarla:sil:ver/set:remove:give> [oyuncuAdı..] <kredi-miktarı..>"));
            return true;
        }

      //  System.out.println("" + args[0] + args[1] + args[2] +  args[3] + args[4]);

        if(args[0].equalsIgnoreCase("ayarla") || args[0].equalsIgnoreCase("set")) {
            Profile targetUser = RedrockPlugin.getInstance().getProfileManager().getProfile(args[1]);

            if (targetUser == null) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cVeritabanında &f'" + args[0] + "' &cismine sahip bir oyuncu bulunamadı."));
                return true;
            }

            Player t = Bukkit.getPlayer(args[1]);
            targetUser.setCredit(Long.parseLong(args[2]));

            sender.sendMessage(C.translate(user.getRankType().getColor() + t.getName() + " &aoyuncusunun kredi değeri &f" + NumberUtil.formatNumberByDecimal(Double.parseDouble(args[2])) + " &aolarak güncellendi."));
            t.sendMessage(C.translate("&aKredi değeriniz &f" + NumberUtil.formatNumberByDecimal(Double.parseDouble(args[2])) + " &aolarak güncellendi."));
            targetUser.saveProfile();
            return true;
        }

        if(args[0].equalsIgnoreCase("sil") || args[0].equalsIgnoreCase("remove")) {
            Profile targetUser = RedrockPlugin.getInstance().getProfileManager().getProfile(args[1]);

            if (targetUser == null) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cVeritabanında &f'" + args[0] + "' &cismine sahip bir oyuncu bulunamadı."));
                return true;
            }

            Player t = Bukkit.getPlayer(args[1]);
            long amount = Long.parseLong(args[2]);

            targetUser.setCredit(targetUser.getCredit() - amount);

            sender.sendMessage(C.translate(user.getRankType().getColor() + t.getName() + " &aoyuncusunun kredi değeri &f" + NumberUtil.formatNumberByDecimal(Double.parseDouble(args[2])) + " &akadar silindi."));
            t.sendMessage(C.translate("&aKredi değeriniz &f" + NumberUtil.formatNumberByDecimal(Double.parseDouble(args[2])) + " &akadar silindi."));
            targetUser.saveProfile();
            return true;
        }

        if(args[0].equalsIgnoreCase("ver") || args[0].equalsIgnoreCase("give")) {
            Profile targetUser = RedrockPlugin.getInstance().getProfileManager().getProfile(args[1]);


            if (targetUser == null) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cVeritabanında &f'" + args[0] + "' &cismine sahip bir oyuncu bulunamadı."));
                return true;
            }

            Player t = Bukkit.getPlayer(args[1]);
            long amount = Long.parseLong(args[2]);

            targetUser.setCredit(targetUser.getCredit() + amount);

            sender.sendMessage(C.translate(user.getRankType().getColor() + t.getName() + " &aoyuncusunun kredi değeri &f" + NumberUtil.formatNumberByDecimal(Double.parseDouble(args[2])) + " &akadar eklendi."));
            t.sendMessage(C.translate("&aKredi değerinize &f" + NumberUtil.formatNumberByDecimal(Double.parseDouble(args[2])) + " &akadar eklendi."));
            targetUser.saveProfile();
            return true;
        }
        return true;
    }
}
