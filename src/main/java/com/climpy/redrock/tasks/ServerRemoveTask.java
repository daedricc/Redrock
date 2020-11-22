package com.climpy.redrock.tasks;

import com.climpy.redrock.RedrockPlugin;
import com.climpy.redrock.redis.RedisManager;
import com.climpy.redrock.server.ServerData;
import com.climpy.redrock.server.ServerState;
import com.climpy.redrock.server.provider.CustomServerProvider;
import com.climpy.torch.TorchPlugin;
import com.climpy.torch.profile.Profile;
import com.climpy.redrock.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;

public class ServerRemoveTask extends BukkitRunnable {
    private final RedrockPlugin redrockPlugin;

    public ServerRemoveTask(RedrockPlugin bedrockPlugin) {
        this.redrockPlugin = bedrockPlugin;
    }

    public void run() {
        RedisManager redisManager = RedrockPlugin.getInstance().getRedisManager();
        Iterator<String> deletedServer = redisManager.getServers().keySet().iterator();
        while (deletedServer.hasNext()) {
            String serverName = deletedServer.next();
            ServerData serverData = redisManager.getServerByName(serverName);
            CustomServerProvider customServerProvider = new CustomServerProvider(serverName);
            long serverLastUpdate = System.currentTimeMillis() - customServerProvider.getServerLastUpdateTime();
            if (serverLastUpdate >= 150000L) {
                if (customServerProvider.getServerState() != ServerState.INACTIVE) {
                    serverData.setServerState(ServerState.INACTIVE);
                    serverData.setOnlinePlayers(0);
                }
                deletedServer.remove();
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    Profile onlineUser = TorchPlugin.getInstance().getProfileManager().getProfile(onlinePlayer.getUniqueId());
                    if (onlineUser.getRank().hasPermission("torch.staff")) {
                        String stringBuilder = C.GRAY + "[Network] " + C.WHITE + "Kaldırılan sunucu, " + serverName + ".";
                        onlinePlayer.sendMessage(stringBuilder);
                    }
                }
            }
        }
    }
}
