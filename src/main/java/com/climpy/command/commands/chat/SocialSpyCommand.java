package com.climpy.command.commands.chat;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.utils.C;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SocialSpyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Profile profile = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());

        boolean socialSpy = !profile.isSocialSpy();
        profile.setSocialSpy(socialSpy);
        profile.toPlayer().sendMessage(C.GOLD + "Casus Modu " + (socialSpy ? C.GREEN + "açıldı." : C.RED + "kapatıldı."));
        profile.saveProfile();
        return true;
    }
}
