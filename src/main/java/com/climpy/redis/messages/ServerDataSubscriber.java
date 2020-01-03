package com.climpy.redis.messages;

import com.climpy.RedrockPlugin;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.redis.RedisManager;
import com.climpy.redis.data.DataSubscriber;
import com.climpy.server.ServerData;
import com.climpy.server.ServerState;
import com.climpy.server.ServerType;
import com.climpy.utils.C;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.net.InetSocketAddress;
import java.util.logging.Level;

public class ServerDataSubscriber extends DataSubscriber {
  public ServerDataSubscriber() {
    super(RedrockPlugin.getInstance(), "server_data");
  }
  
  public void handleMessage(JsonObject object) {
    String serverName = object.get("server_name").getAsString();
    JsonObject data = object.get("data").getAsJsonObject();
    int serverID = data.get("server_id").getAsInt();
    ServerType serverType = ServerType.getServerType(data.get("server_type").getAsString());
    ServerState serverState = ServerState.getStateType(data.get("server_state").getAsString());
    String[] serverIps = data.get("server_ip").getAsString().split(":");
    int PORT_NUMBER = Integer.valueOf(serverIps[1]).intValue();
    String IP_ADDRESS = serverIps[0];
    InetSocketAddress socketIP = InetSocketAddress.createUnresolved(IP_ADDRESS, PORT_NUMBER);
    int onlinePlayers = data.get("online_players").getAsInt();
    int maxPlayers = data.get("maximum_players").getAsInt();
    boolean whitelistStatus = data.get("whitelist_status").getAsBoolean();
    long rebootTime = data.get("server_reboot_time").getAsLong();
    long lastUpdate = 0L;
    if (data.get("server_last_update") != null) {
      lastUpdate = data.get("server_last_update").getAsLong();
      long LAST_UPDATE = System.currentTimeMillis() - lastUpdate;
      if (serverState == ServerState.INACTIVE &&
        LAST_UPDATE >= 150000L)
        return; 
    } else if (serverState == ServerState.INACTIVE) {
      return;
    } 
    long serverUpTime = data.get("server_up_time").getAsLong();
    double tpsStatus = data.get("tps_status").getAsDouble();
    RedisManager redisManager = RedrockPlugin.getInstance().getRedisManager();
    ServerData serverData = redisManager.getServerByName(serverName);
    if (serverData == null) {
      serverData = new ServerData();
      for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
        User onlineUser = ProfilePlugin.getInstance().getUserManager().getUser(onlinePlayer.getUniqueId());
        if (onlineUser.getRankType().isAboveOrEqual(RankType.CHAT_MOD)) {
          String stringBuilder = C.GRAY + "[M] " + C.WHITE + "Sunucu ağa eklendi. (" + serverName + ")";
          onlinePlayer.sendMessage(stringBuilder);
        }
      }
    } 
    serverData.setServerName(serverName);
    serverData.setServerID(serverID);
    serverData.setServerType(serverType);
    serverData.setServerState(serverState);
    serverData.setIP(socketIP);
    serverData.setOnlinePlayers(onlinePlayers);
    serverData.setMaxPlayers(maxPlayers);
    serverData.setWhitelisted(whitelistStatus);
    serverData.setRebootTime(rebootTime);
    serverData.setLastUpdate(lastUpdate);
    serverData.setUpTime(serverUpTime);
    serverData.setTpsStatus(tpsStatus);
    redisManager.serverDataUpdate(serverName, serverData);
  }
}
