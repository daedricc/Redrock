package com.climpy.redrock.server.commands;

import com.climpy.redrock.RedrockPlugin;
import com.climpy.redrock.commands.RedrockCommand;
import com.climpy.redrock.commands.RedrockResultCommand;
import com.climpy.redrock.redis.RedisManager;
import com.climpy.redrock.server.ServerType;
import com.climpy.redrock.utils.C;
import com.climpy.torch.TorchPlugin;
import com.climpy.torch.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerCommand extends RedrockCommand {
    public ServerCommand() {
        super("server", "Sunucuları gösterir.");
    }

    @Override
    public RedrockResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        Profile profile = TorchPlugin.getInstance().getProfileManager().getProfile(player.getName());
        if (!profile.toPlayer().hasPermission("redrock.server.show") || (!profile.toPlayer().hasPermission("redrock.server.*"))) {
            return RedrockResultCommand.INVALID_PERMISSION;
        }

        RedisManager redisManager = RedrockPlugin.getInstance().getRedisManager();
        if (redisManager.getServers().isEmpty()) {
            player.sendMessage(C.RED + "Sunucu değiştiremezsiniz, uygun sunucu bulunamıyor.");
            return RedrockResultCommand.SUCCESS;
        }

        player.sendMessage(C.CHAT_BAR);
        if (args.length == 1) {
            ServerType serverType;

            try {
                serverType = ServerType.getServerType(args[0]);
            } catch (Exception ex) {
                sender.sendMessage(C.RED + "Girilen isimde sunucu bulunamadı.");
                return RedrockResultCommand.SUCCESS;
            }

            if (serverType == ServerType.UNKNOWN) {
                sender.sendMessage(C.RED + "Girilen türde sunucu bulunamadı.");
                return RedrockResultCommand.SUCCESS;
            }
            String serverName = serverType.getDisplayName();
            sender.sendMessage(serverName + " türündeki sunucular" + C.RESET + redisManager.getServersWithState(serverType));
        } else {
            sender.sendMessage("Ağdaki Sunucular: " + C.RESET + redisManager.getServersWithState());
        }
        player.sendMessage(C.CHAT_BAR);
        return RedrockResultCommand.SUCCESS;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            List<String> typeNames = new ArrayList<>();
            for (ServerType serverType : ServerType.values())
                typeNames.add(serverType.getInternalName());
            return typeNames;
        }
        return Collections.emptyList();
    }
}
