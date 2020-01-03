package com.climpy.tasks;

import com.climpy.RedrockPlugin;
import com.climpy.redis.data.DataPublisher;
import com.climpy.server.ServerState;
import com.climpy.server.ServerType;
import com.climpy.server.provider.DefaultServerProvider;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class ServerDataTask extends BukkitRunnable {
  private final RedrockPlugin redrockPlugin;
  
  public ServerDataTask(RedrockPlugin redrockPlugin) {
    this.redrockPlugin = redrockPlugin;
  }
  
  public void run() {
    JsonObject jsonObject = new JsonObject();
    JsonObject serverData = new JsonObject();
    DefaultServerProvider defaultServerProvider = new DefaultServerProvider();
    ServerType serverType = defaultServerProvider.getServerType();
    ServerState serverState = defaultServerProvider.getServerState();
    RedrockPlugin.getInstance().setLastUpTime(System.currentTimeMillis());
    serverData.addProperty("server_type", serverType.getInternalName().toLowerCase());
    serverData.addProperty("server_state", serverState.getInternalName().toLowerCase());
    serverData.addProperty("server_id", Integer.valueOf(defaultServerProvider.getServerID()));
    serverData.addProperty("server_ip", String.valueOf(defaultServerProvider.getServerIP()));
    serverData.addProperty("server_up_time", Long.valueOf(defaultServerProvider.getServerStartTime()));
    serverData.addProperty("server_last_update", Long.valueOf(defaultServerProvider.getServerLastUpdateTime()));
    serverData.addProperty("server_reboot_time", Long.valueOf(defaultServerProvider.getServerRebootTime()));
    serverData.addProperty("whitelist_status", Boolean.valueOf(defaultServerProvider.getServerWhitelist()));
    serverData.addProperty("online_players", Integer.valueOf(defaultServerProvider.getPlayerCount()));
    serverData.addProperty("maximum_players", Integer.valueOf(defaultServerProvider.getMaxPlayerCount()));
    serverData.addProperty("tps_status", 0);
    jsonObject.addProperty("server_name", defaultServerProvider.getServerName());
    jsonObject.add("data", (JsonElement)serverData);
    try {
      DataPublisher dataPublisher = RedrockPlugin.getInstance().getRedisConnection().getDataPublisher();
      dataPublisher.write("server_data", jsonObject);
    } catch (IndexOutOfBoundsException|NullPointerException ex) {
      this.redrockPlugin.getLogger().log(Level.WARNING, "Sunucu verileri kaydedilrken hata oluşut: " + defaultServerProvider.getServerName());
    } 
  }
}
