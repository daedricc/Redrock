package com.climpy.server.provider;

import com.climpy.RedrockPlugin;
import com.climpy.server.ServerState;
import com.climpy.server.ServerType;
import com.climpy.tasks.Lag;
import org.bukkit.Bukkit;

import java.net.InetSocketAddress;

public class DefaultServerProvider implements IServerProvider {
    public String getServerName() {
        return RedrockPlugin.getInstance().getServerName() + getServerID();
    }

    public int getServerID() {
        return RedrockPlugin.getInstance().getServerID();
    }

    public boolean getServerWhitelist() {
        return RedrockPlugin.getInstance().getServer().hasWhitelist();
    }

    public InetSocketAddress getServerIP() {
        return InetSocketAddress.createUnresolved(Bukkit.getServer().getIp(), Bukkit.getServer().getPort());
    }

    public int getPlayerCount() {
        return RedrockPlugin.getInstance().getServer().getOnlinePlayers().size();
    }

    public int getMaxPlayerCount() {
        return RedrockPlugin.getInstance().getServer().getMaxPlayers();
    }

    public ServerState getServerState() {
        return RedrockPlugin.getInstance().getServerState();
    }

    public ServerType getServerType() {
        return RedrockPlugin.getInstance().getServerType();
    }

    public long getServerStartTime() {
        return RedrockPlugin.getInstance().getStartTime();
    }

    public long getServerLastUpdateTime() {
        return RedrockPlugin.getInstance().getLastUpTime();
    }

    public long getServerRebootTime() {
        return System.currentTimeMillis();
    }

    public double getServerTPSStatus() {
        return Lag.getTPS();
    }
}
