package com.climpy.commands.network;

import com.climpy.RedrockPlugin;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.redis.RedisManager;
import com.climpy.server.ServerType;
import com.climpy.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getName());
        if(!user.getRankType().isAboveOrEqual(RankType.MOD)) {
            player.sendMessage(C.RED + "Bu işlem için " + RankType.MOD.getDisplayName() + C.RED + " ve üzeri yetki gereklidir.");
            return true;
        }

        RedisManager redisManager = RedrockPlugin.getInstance().getRedisManager();
        if(redisManager.getServers().isEmpty()) {
            player.sendMessage(C.RED + "Sunucu değiştiremezsiniz, uygun sunucu bulunamıyor.");
            return true;
        }

        player.sendMessage(C.translate("    &7&m--------------&f &4&lMoosy &f&lNetwork &7&m--------------&f "));
        if (args.length == 1) {
            ServerType serverType;
            try {
                serverType = ServerType.getServerType(args[0]);
            } catch (Exception ex) {
                sender.sendMessage(C.RED + "Girilen isimde sunucu bulunamadı.");
                return true;
            }
            if (serverType == ServerType.UNKNOWN) {
                sender.sendMessage(C.RED + "Girilen türde sunucu bulunamadı.");
                return true;
            }
            String serverName = serverType.getDisplayName();
            sender.sendMessage(serverName + " türündei Redis sunucular" + C.RESET + redisManager.getServersWithState(serverType));
        } else {
            sender.sendMessage("Ağdaki Sunucular: " + C.RESET + redisManager.getServersWithState());
        }
        player.sendMessage(C.translate("    &7&m--------------&f &4&lMoosy &f&lNetwork &7&m--------------&f "));
        return true;
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
