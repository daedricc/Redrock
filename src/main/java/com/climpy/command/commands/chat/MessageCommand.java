package com.climpy.command.commands.chat;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.manager.ChatManager;
import com.climpy.profile.Profile;
import com.climpy.profile.ProfileManager;
import com.climpy.profile.rank.RankType;
import com.climpy.utils.C;
import com.climpy.utils.MiscUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Profile profile = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        Player player = Bukkit.getPlayer(sender.getName());

        if(args.length < 2) {
            sender.sendMessage(C.RED + "Kullanım: /" + label + " <oyuncuAdı> [Mesaj..]" );
            return true;
        }

        ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
        Profile targetProfile = profileManager.getProfile(args[0]);
        if(targetProfile ==  null) {
            player.sendMessage(C.WHITE + targetProfile + C.RED + " sunucuda bulunamadı.");
            return true;
        }

        String privateMessage = MiscUtils.joinStrings(args, 1);

        ChatManager chatManager = RedrockPlugin.getInstance().getChatManager();
        chatManager.sendPrivateMessage(player.getUniqueId(), targetProfile.getUniqueUUID(), privateMessage);
        return true;
    }
}
