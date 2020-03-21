package com.climpy.core.commands;

import com.climpy.RedrockPlugin;
import com.climpy.utils.C;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

public class InfoCommand implements CommandExecutor {
    public PluginDescriptionFile pluginInfo = RedrockPlugin.getInstance().getDescription();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(C.CHAT_BAR);
        sender.sendMessage(C.translate("&6Redrock Plugin"));
        sender.sendMessage(" ");
        sender.sendMessage(C.translate("&7Versiyon: &f" + pluginInfo.getVersion()));
        sender.sendMessage(C.translate("&7Yapımcı: &f" + pluginInfo.getAuthors()));
        sender.sendMessage(C.CHAT_BAR);
        return true;
    }
}
