package com.climpy.tasks;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.redis.RedisManager;
import com.climpy.server.ServerData;
import com.climpy.server.ServerState;
import com.climpy.server.provider.CustomServerProvider;
import com.climpy.utils.C;
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
                    Profile onlineUser = RedrockPlugin.getInstance().getProfileManager().getProfile(onlinePlayer.getUniqueId());
                    if (onlineUser.getRankType().isAboveOrEqual(RankType.CHAT_MOD)) {
                        String stringBuilder = C.GRAY + "[Network] " + C.WHITE + "Kaldırılan sunucu, " + serverName + ".";
                        onlinePlayer.sendMessage(stringBuilder);
                    }
                }
            }
        }
    }
}
