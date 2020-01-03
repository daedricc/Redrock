package com.climpy.server.provider;

import com.climpy.RedrockPlugin;
import com.climpy.redis.RedisManager;
import com.climpy.server.ServerData;
import com.climpy.server.ServerState;
import com.climpy.server.ServerType;
import java.net.InetSocketAddress;

public class CustomServerProvider implements IServerProvider {
  private ServerData serverData;
  
  public CustomServerProvider(String serverName) {
    RedisManager redisManager = RedrockPlugin.getInstance().getRedisManager();
    this.serverData = redisManager.getServerByName(serverName);
  }
  
  public String getServerName() {
    return this.serverData.getServerName();
  }
  
  public int getServerID() {
    return this.serverData.getServerID();
  }
  
  public boolean getServerWhitelist() {
    return this.serverData.isWhitelisted();
  }
  
  public InetSocketAddress getServerIP() {
    return this.serverData.getIP();
  }
  
  public int getPlayerCount() {
    return this.serverData.getOnlinePlayers();
  }
  
  public int getMaxPlayerCount() {
    return this.serverData.getMaxPlayers();
  }
  
  public ServerState getServerState() {
    return this.serverData.getServerState();
  }
  
  public ServerType getServerType() {
    return this.serverData.getServerType();
  }
  
  public long getServerStartTime() {
    return this.serverData.getUpTime();
  }
  
  public long getServerLastUpdateTime() {
    return this.serverData.getLastUpdate();
  }

  public double getServerTPSStatus() {
    return this.serverData.getTpsStatus();
  }

  public long getServerRebootTime() {
    return this.serverData.getRebootTime();
  }
}
