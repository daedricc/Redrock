package com.climpy.redrock.server.commands;

import com.climpy.redrock.RedrockPlugin;
import com.climpy.redrock.commands.RedrockCommand;
import com.climpy.redrock.commands.RedrockResultCommand;
import com.climpy.redrock.tasks.ServerDataTask;
import com.climpy.torch.TorchPlugin;
import com.climpy.torch.profile.Profile;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;

public class ServerUpdateCommand extends RedrockCommand {
    public ServerUpdateCommand() {
        super("serverupdate");
    }

    @Override
    public RedrockResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        Profile user = TorchPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        if (!user.toPlayer().hasPermission("redrock.moderator")) {
            return RedrockResultCommand.INVALID_PERMISSION;
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

        return RedrockResultCommand.SUCCESS;
    }
}
