package com.climpy.command.extnds.commands.chat;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.utils.C;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PrivateMessagesVisibleCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Profile profile = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());

        boolean privateMessagesDisable = !profile.isMessagesVisible();
        profile.setMessagesVisible(privateMessagesDisable);
        profile.toPlayer().sendMessage(C.GOLD + "Özel Mesajlar " + (privateMessagesDisable ? C.GREEN + "açıldı." : C.RED + " kapatıldı."));
        profile.saveProfile();
        return true;
    }
}
