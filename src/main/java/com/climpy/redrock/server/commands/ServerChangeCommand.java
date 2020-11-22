package com.climpy.redrock.server.commands;

import com.climpy.redrock.Locale;
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
        super("serverchange", "Change your current server");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Profile user = TorchPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        if (!user.getRank().hasPermission("torch.staff")) {
            sender.sendMessage(Locale.format(Locale.NO_PERM));
            return true;
        }

        RedisManager redisManager = RedrockPlugin.getInstance().getRedisManager();
        DefaultServerProvider defaultServerProvider = new DefaultServerProvider();
        String currentlyServerName = defaultServerProvider.getServerName();
        if (args.length < 1) {
            sender.sendMessage(ChatColor.BLUE + "Şu anda bağlı olduğunuz sunucu: " + ChatColor.WHITE + currentlyServerName);
            sender.sendMessage(ChatColor.RED + "Sunucu değiştirme kullanımı: " + ChatColor.WHITE + "/" + label + " <sunucuAdı>");
            sender.sendMessage(ChatColor.AQUA + "Geçerli sunucular: " + ChatColor.WHITE + redisManager.getServerList());
            return true;
        }
        ServerData serverData = redisManager.getServerByName((args.length == 2) ? args[1] : args[0]);
        if (serverData == null) {
            sender.sendMessage(ChatColor.RED + "Girilen isimde bir sunucu bulunamıyor.");
            return true;
        }
        CustomServerProvider customServerProvider = new CustomServerProvider(serverData.getServerName());
        ServerState serverState = ServerState.INACTIVE;
        if (customServerProvider.getServerState() == serverState) {
            sender.sendMessage(ChatColor.RED + "Girilen sunucu anda " + serverState.getDisplayName() + " durumdada.");
            return true;
        }
        String serverName = customServerProvider.getServerName();
        if (currentlyServerName.equals(serverName)) {
            sender.sendMessage(ChatColor.RED + "Zaten anda " + ChatColor.WHITE + serverName + ChatColor.RED + " sunucusuna bağlısın.");
            return true;
        }

        redisManager.sendRedisServer(user.getUniqueUUID(), serverData);
        sender.sendMessage(ChatColor.GOLD + "Gönderiliyor: " + ChatColor.WHITE + serverName + ChatColor.GOLD + "..");
        return true;
    }

    @Override
    public RedrockResultCommand execute(CommandSender commandSender, Command command, String label, String[] args) {
        return null;
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
