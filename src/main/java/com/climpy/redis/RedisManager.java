package com.climpy.redis;

import com.climpy.RedrockPlugin;
import com.climpy.listener.ChannelListener;
import com.climpy.redis.messages.MessageSubscriber;
import com.climpy.redis.data.DataPublisher;
import com.climpy.server.ServerData;
import com.climpy.server.ServerState;
import com.climpy.server.ServerType;
import com.climpy.server.provider.DefaultServerProvider;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class RedisManager {
    private final RedrockPlugin redrockPlugin;

    private Map<String, ServerData> servers;

    public RedrockPlugin getRedrockPlugin() {
        return this.redrockPlugin;
    }

    public Map<String, ServerData> getServers() {
        return this.servers;
    }

    private final String STRING_SEPARATOR = ", ";

    public String getSTRING_SEPARATOR() {
        getClass();
        return ", ";
    }

    public RedisManager(RedrockPlugin redrockPlugin) {
        this.redrockPlugin = redrockPlugin;
        this.servers = new HashMap<>();
    }

    public ServerData getServerByName(String serverName) {
        if (this.servers.isEmpty())
            return null;
        for (ServerData serverData : this.servers.values()) {
            if (serverName.equals(serverData.getServerName()))
                return serverData;
        }
        return null;
    }

    public String getServerList() {
        if (this.servers.isEmpty())
            return "Sunucu bulunamadı.";
        StringBuilder stringBuilder = new StringBuilder();
        for (ServerData serverData : this.servers.values()) {
            if (serverData == null)
                continue;
            stringBuilder.append(serverData.getServerName());
            getClass();
            stringBuilder.append(", ");
        }
        return separatorCheck(stringBuilder);
    }

    public String getServersWithState() {
        if (this.servers.isEmpty())
            return "Sunucu bulunamadı.";
        return getServerBuilder(new ArrayList<>(this.servers.values()));
    }

    public String getServersWithState(ServerType serverType) {
        if (this.servers.isEmpty())
            return "Sunucu bulunamadı.";
        List<ServerData> customServer = (List<ServerData>)this.servers.values().stream().filter(serverData -> (serverData.getServerType() == serverType)).collect(Collectors.toList());
        return getServerBuilder(customServer);
    }

    public int getNetworkOnlinePlayers() {
        int onlineCount = 0;
        for (ServerData serverData : this.servers.values()) {
            if (serverData == null) {
                onlineCount += 0;
                continue;
            }
            int serverOnlineCount = serverData.getOnlinePlayers();
            onlineCount += serverOnlineCount;
        }
        return onlineCount;
    }

    public int getNetworkMaxPlayers() {
        int maximumPlayersCount = 0;
        for (ServerData serverData : this.servers.values()) {
            if (serverData == null) {
                maximumPlayersCount += 0;
                continue;
            }
            int serverMaxPlayers = serverData.getMaxPlayers();
            maximumPlayersCount += serverMaxPlayers;
        }
        return maximumPlayersCount;
    }

    public ServerData getSuitableLobby() {
        return getSuitableServer(ServerType.LOBI);
    }

    public ServerData getSuitableServer(ServerType serverType) {
        List<ServerData> lobbies = (List<ServerData>)this.servers.values().stream().filter(serverData -> (serverData.getServerType() == serverType)).filter(serverData -> (serverData.getServerState() == ServerState.ACTIVE)).collect(Collectors.toList());
        DefaultServerProvider defaultServerProvider = new DefaultServerProvider();
        if (lobbies.isEmpty() || (lobbies.size() == 1 && defaultServerProvider.getServerName().equals(((ServerData)lobbies.get(0)).getServerName())))
            return null;
        Comparator<ServerData> serverDataComparator = Comparator.comparingInt(ServerData::getOnlinePlayers);
        lobbies.sort(serverDataComparator);
        return lobbies.get(0);
    }

    public void kickRedisServer(UUID playerUUID, ServerData serverData, String reason) {
        Player targetPlayer = Bukkit.getPlayer(playerUUID);
        DefaultServerProvider defaultServerProvider = new DefaultServerProvider();
        ServerData connectingServer = getServerByName(serverData.getServerName());
        if (connectingServer == null)
            return;
        ChannelListener.sendToServer(targetPlayer, connectingServer.getServerName());
        DataPublisher dataPublisher = RedrockPlugin.getInstance().getRedisConnection().getDataPublisher();
        JsonObject messageObject = new JsonObject();
        MessageSubscriber.MessageType messageType = MessageSubscriber.MessageType.PLAYER;
        messageObject.addProperty("type", messageType.getInternalName());
        messageObject.addProperty("message", ChatColor.RED + "Sunucudan atıldı" + ChatColor.LIGHT_PURPLE + defaultServerProvider
                .getServerName() + ChatColor.RED + ": " + ChatColor.YELLOW + reason);
        messageObject.addProperty("value", targetPlayer.getUniqueId().toString());
        dataPublisher.write("message", messageObject);
    }

    public void sendRedisServer(UUID playerUUID, ServerData serverData) {
        Player targetPlayer = Bukkit.getPlayer(playerUUID);
        ServerData connectingServer = getServerByName(serverData.getServerName());
        if (connectingServer == null)
            return;
        String serverName = connectingServer.getServerName();
        ChannelListener.sendToServer(targetPlayer, serverName);
    }

    public void serverDataUpdate(String serverName, ServerData serverData) {
        if (!this.servers.containsKey(serverName)) {
            this.servers.put(serverName, serverData);
            return;
        }
        for (ServerData oldData : this.servers.values()) {
            if (!serverName.equals(oldData.getServerName()))
                continue;
            oldData.setServerName(serverData.getServerName());
            oldData.setServerID(serverData.getServerID());
            oldData.setServerState(serverData.getServerState());
            oldData.setServerType(serverData.getServerType());
            oldData.setTpsStatus(serverData.getTpsStatus());
            oldData.setIP(serverData.getIP());
            oldData.setUpTime(serverData.getUpTime());
            oldData.setLastUpdate(serverData.getLastUpdate());
            oldData.setWhitelisted(serverData.isWhitelisted());
            oldData.setOnlinePlayers(serverData.getOnlinePlayers());
            oldData.setMaxPlayers(serverData.getMaxPlayers());
        }
    }

    public void serverRemove(String serverName) {
        if (!this.servers.containsKey(serverName))
            return;
        this.servers.remove(serverName);
    }

    private String getServerBuilder(List<ServerData> customServers) {
        StringBuilder stringBuilder = new StringBuilder();
        for (ServerData serverData : customServers) {
            if (serverData != null) {
                String serverName = serverData.getServerName();
                if (serverData.getServerState() == ServerState.INACTIVE) {
                    stringBuilder.append(ChatColor.RED).append(serverName).append(ChatColor.WHITE).append(" (").append(ChatColor.AQUA).append(serverData.getServerType()).append(ChatColor.WHITE).append(") [").append(ChatColor.GOLD).append(serverData.getOnlinePlayers()).append(ChatColor.WHITE).append("]");
                    getClass();
                    stringBuilder.append(", ");
                    continue;
                }
                if (serverData.getServerState() == ServerState.ACTIVE) {
                    stringBuilder.append(ChatColor.GREEN).append(serverName).append(ChatColor.WHITE).append(" (").append(ChatColor.AQUA).append(serverData.getServerType()).append(ChatColor.WHITE).append(") [").append(ChatColor.GOLD).append(serverData.getOnlinePlayers()).append(ChatColor.WHITE).append("]");
                    getClass();
                    stringBuilder.append(", ");
                    continue;
                }
                stringBuilder.append(ChatColor.GRAY).append(serverName).append(ChatColor.WHITE).append(" (").append(ChatColor.AQUA).append(serverData.getServerType()).append(ChatColor.WHITE).append(") [").append(ChatColor.GOLD).append(serverData.getOnlinePlayers()).append(ChatColor.WHITE).append("]");
                getClass();
                stringBuilder.append(", ");
            }
        }
        return separatorCheck(stringBuilder);
    }

    private String separatorCheck(StringBuilder stringBuilder) {
        String toReturn = stringBuilder.toString();
        getClass();
        if (toReturn.endsWith(", ")) {
            getClass();
            toReturn = toReturn.substring(0, toReturn.length() - ", ".length());
        }
        return toReturn;
    }
}
