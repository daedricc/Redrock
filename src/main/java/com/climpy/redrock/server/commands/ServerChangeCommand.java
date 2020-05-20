package com.climpy.redrock.server.commands;

import com.climpy.redrock.RedrockPlugin;
import com.climpy.redrock.commands.RedrockCommand;
import com.climpy.redrock.commands.RedrockResultCommand;
import com.climpy.redrock.redis.RedisManager;
import com.climpy.redrock.server.ServerData;
import com.climpy.redrock.server.ServerState;
import com.climpy.redrock.server.provider.CustomServerProvider;
import com.climpy.redrock.server.provider.DefaultServerProvider;
import com.climpy.torch.TorchPlugin;
import com.climpy.torch.profile.Profile;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerChangeCommand extends RedrockCommand {
    public ServerChangeCommand() {
        super("serverchange", "Sunucuyu değiştirir.");
    }

    @Override
    public RedrockResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        Profile profile = TorchPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        if (!profile.toPlayer().hasPermission("redrock.server.change") || (!profile.toPlayer().hasPermission("redrock.server.*"))) {
            return RedrockResultCommand.INVALID_PERMISSION;
        }

        RedisManager redisManager = RedrockPlugin.getInstance().getRedisManager();
        DefaultServerProvider defaultServerProvider = new DefaultServerProvider();
        String currentlyServerName = defaultServerProvider.getServerName();
        if (args.length < 1) {
            sender.sendMessage(ChatColor.BLUE + "Şu anda bağlı olduğunuz sunucu: " + ChatColor.WHITE + currentlyServerName);
            sender.sendMessage(ChatColor.RED + "Sunucu değiştirme kullanımı: " + ChatColor.WHITE + "/" + label + " <sunucuAdı>");
            sender.sendMessage(ChatColor.AQUA + "Geçerli sunucular: " + ChatColor.WHITE + redisManager.getServerList());
            return RedrockResultCommand.SUCCESS;
        }

        ServerData serverData = redisManager.getServerByName((args.length == 2) ? args[1] : args[0]);
        if (serverData == null) {
            sender.sendMessage(ChatColor.RED + "Girilen isimde bir sunucu bulunamıyor.");
            return RedrockResultCommand.SUCCESS;
        }
        CustomServerProvider customServerProvider = new CustomServerProvider(serverData.getServerName());
        ServerState serverState = ServerState.INACTIVE;
        if (customServerProvider.getServerState() == serverState) {
            sender.sendMessage(ChatColor.RED + "Girilen sunucu anda " + serverState.getDisplayName() + " durumdada.");
            return RedrockResultCommand.SUCCESS;
        }
        String serverName = customServerProvider.getServerName();
        if (currentlyServerName.equals(serverName)) {
            sender.sendMessage(ChatColor.RED + "Zaten anda " + ChatColor.WHITE + serverName + ChatColor.RED + " sunucusuna bağlısın.");
            return RedrockResultCommand.SUCCESS;
        }

        redisManager.sendRedisServer(profile.getUniqueUUID(), serverData);
        sender.sendMessage(ChatColor.GOLD + "Gönderiliyor: " + ChatColor.WHITE + serverName + ChatColor.GOLD + "..");
        return RedrockResultCommand.SUCCESS;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 2) {
            List<String> serverNames = new ArrayList<>();
            RedisManager redisManager = RedrockPlugin.getInstance().getRedisManager();
            for (ServerData serverData : redisManager.getServers().values()) {
                if (serverData.getServerName().toLowerCase().startsWith(args[1].toLowerCase()))
                    serverNames.add(serverData.getServerName());
            }
            return serverNames;
        }
        return Collections.emptyList();
    }
}
