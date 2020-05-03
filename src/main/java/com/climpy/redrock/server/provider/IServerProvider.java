package com.climpy.redrock.server.provider;

import com.climpy.redrock.server.ServerState;
import com.climpy.redrock.server.ServerType;

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
