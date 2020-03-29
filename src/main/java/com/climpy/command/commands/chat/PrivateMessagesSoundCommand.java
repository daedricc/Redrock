package com.climpy.command.commands.chat;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.utils.C;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PrivateMessagesSoundCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Profile profile = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());

        boolean privateMessagesSound = !profile.isMessagingSounds();
        profile.setMessagingSounds(privateMessagesSound);
        profile.toPlayer().sendMessage(C.GOLD + "Özel Mesajlar için sesler " + (privateMessagesSound ? C.GREEN + " açıldı." : C.RED + "kapatıldı."));
        profile.saveProfile();
        return true;
    }
}
