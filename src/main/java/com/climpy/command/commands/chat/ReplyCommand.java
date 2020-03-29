package com.climpy.command.commands.chat;

import com.climpy.RedrockPlugin;
import com.climpy.manager.ChatManager;
import com.climpy.profile.Profile;
import com.climpy.profile.ProfileManager;
import com.climpy.utils.C;
import com.climpy.utils.MiscUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ReplyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());

        ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
        Profile profile = profileManager.getProfile(player.getUniqueId());
        UUID lastRepliedPlayer = profile.getLastRepliedPlayer();
        if (lastRepliedPlayer == null) {
            player.sendMessage(C.RED + "Daha önce herhangi bir oyuncuya mesaj atmamışsınız.");
            return true;
        }

        Profile targetProfile = profileManager.getProfile(profile.getLastRepliedPlayer());
        Player targetPlayer = Bukkit.getPlayer(targetProfile.getUniqueUUID());
        if (targetPlayer == null) {
            sender.sendMessage(C.RED + "En son mesaj yazılan kişi şu anda sunucu değil.");
            return true;
        }

        String privateMessage = MiscUtils.joinStrings(args, 1);

        ChatManager chatManager = RedrockPlugin.getInstance().getChatManager();
        chatManager.sendPrivateMessage(player.getUniqueId(), targetProfile.getUniqueUUID(), privateMessage);

        return true;
    }
}
