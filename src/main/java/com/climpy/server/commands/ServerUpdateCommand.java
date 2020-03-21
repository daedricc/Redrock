package com.climpy.server.commands;

import com.climpy.RedrockPlugin;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.tasks.ServerDataTask;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;

public class ServerUpdateCommand implements CommandExecutor  {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        if (!user.getRankType().isAboveOrEqual(RankType.CO_OWNER)) {
            sender.sendMessage(ChatColor.RED + "Bu için " + RankType.CO_OWNER.getColor() + RankType.CO_OWNER
                    .getDisplayName() + ChatColor.RED + " üzeri gerekir.");
            return true;
      }

        Command.broadcastCommandMessage(sender, ChatColor.GOLD + "Sunucu güncellemesi başlatılıyor..");
        try {
            (new ServerDataTask(RedrockPlugin.getInstance())).run();
            RedrockPlugin.getInstance().getLogger().log(Level.WARNING, "[Sunucu] Sunucu verisi başarıyla güncellendi.");
                    Command.broadcastCommandMessage(sender, ChatColor.LIGHT_PURPLE + "Sunucu verisi başarıyla güncellendi.");
        } catch (NullPointerException ex) {
            Command.broadcastCommandMessage(sender, ChatColor.RED.toString() + ChatColor.BOLD + "Güncelleme sırasında bir hata meydana geldi!");
            RedrockPlugin.getInstance().getLogger().log(Level.SEVERE, "Hata: {0}", ex.getMessage());
        }
        return true;
    }
}
