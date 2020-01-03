package com.climpy.server.provider;

import com.climpy.server.ServerState;
import com.climpy.server.ServerType;
import java.net.InetSocketAddress;

public interface IServerProvider {
  String getServerName();
  
  int getServerID();
  
  boolean getServerWhitelist();

  InetSocketAddress getServerIP();
  
  int getPlayerCount();
  
  int getMaxPlayerCount();
  
  ServerState getServerState();
  
  ServerType getServerType();
  
  long getServerStartTime();
  
  long getServerLastUpdateTime();

  long getServerRebootTime();

  double getServerTPSStatus();
}
