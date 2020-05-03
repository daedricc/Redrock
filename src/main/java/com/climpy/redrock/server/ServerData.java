package com.climpy.redrock.server;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

import java.net.InetSocketAddress;

@Getter
@Setter
public class ServerData {
    private String serverName;
    private InetSocketAddress iP;
    private ServerState serverState = ServerState.INACTIVE;
    private ServerType serverType = ServerType.UNKNOWN;
    private boolean whitelisted;
    private int serverID;
    private int onlinePlayers;
    private int maxPlayers;
    private long upTime;
    private long lastUpdate;
    private long rebootTime;
    private double tpsStatus;

    public String getServerStatus() {
        if (this.serverState == ServerState.SETUP)
            return ChatColor.DARK_RED + "Açılış sürüyor..";
        if (this.serverState == ServerState.INACTIVE)
            return ChatColor.RED + "Kapalı";
        return this.whitelisted ? (ChatColor.YELLOW + "Beyazliste") : (ChatColor.GREEN + "Çevrimiçi");
    }
}
