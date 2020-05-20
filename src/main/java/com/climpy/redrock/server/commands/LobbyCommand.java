package com.climpy.redrock.server.commands;

import com.climpy.redrock.RedrockPlugin;
import com.climpy.redrock.commands.RedrockCommand;
import com.climpy.redrock.commands.RedrockResultCommand;
import com.climpy.redrock.redis.RedisManager;
import com.climpy.redrock.server.ServerData;
import com.climpy.redrock.server.provider.CustomServerProvider;
import com.climpy.torch.TorchPlugin;
import com.climpy.torch.profile.Profile;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class LobbyCommand extends RedrockCommand {
    public LobbyCommand() {
        super("lobby", "Ana lobiye gönderir.", new String[]{"hub", "lobi"});
    }

    @Override
    public RedrockResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        Profile basicUser = TorchPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        RedisManager redisManager = RedrockPlugin.getInstance().getRedisManager();
        ServerData serverData = redisManager.getSuitableLobby();
        if (serverData == null) {
            sender.sendMessage(ChatColor.RED + "Hub sunucusu bulunamıyor, bir süre sonra tekrar deneyin.");
            return RedrockResultCommand.SUCCESS;
        }

        CustomServerProvider customServerProvider = new CustomServerProvider(serverData.getServerName());
        sender.sendMessage(ChatColor.GREEN + "Gönderiliyorsunuz: " + ChatColor.WHITE + "Hub #" + customServerProvider.getServerID() + ChatColor.GREEN + "..");
        redisManager.sendRedisServer(basicUser.getUniqueUUID(), serverData);
        return RedrockResultCommand.SUCCESS;
    }
}
