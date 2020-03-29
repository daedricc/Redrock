package com.climpy.listener;

import com.climpy.RedrockPlugin;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.UUID;
import java.util.logging.Level;

public class ChannelListener implements PluginMessageListener, Listener {
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (channel.equals("BungeeCord") || channel.equals("RedisBungee")) {
            // System.out.println("BungeeCord ve ya RedisBungee gelen değer işlendi.");
        }
    }

    public static void sendToServer(Player targetPlayer, String serverName) {
        if (targetPlayer == null || serverName.isEmpty())
            return;
        ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
        try {
            dataOutput.writeUTF("Connect");
            dataOutput.writeUTF(serverName);
            targetPlayer.sendPluginMessage((Plugin) RedrockPlugin.getInstance(), "BungeeCord", dataOutput.toByteArray());
        } catch (Exception ex) {
            RedrockPlugin.getInstance().getLogger().log(Level.WARNING, ex.getMessage());
        }
    }

    public static void kickPlayer(UUID uniqueUUID, String reason) {
        Validate.notNull(uniqueUUID, "Girideboolamaz!");
        Validate.notNull(reason, "Girideboolamaz!");
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        Player player = Bukkit.getPlayer(uniqueUUID);
        try {
            out.writeUTF("KickPlayer");
            out.writeUTF(player.getName());
            out.writeUTF(reason);
            player.sendPluginMessage((Plugin) RedrockPlugin.getInstance(), "BungeeCord", out.toByteArray());
        } catch (Exception ex) {
            RedrockPlugin.getInstance().getLogger().log(Level.WARNING, ex.getMessage());
        }
    }

    public static void init() {
        RedrockPlugin.getInstance().getServer().getMessenger().registerOutgoingPluginChannel((Plugin) RedrockPlugin.getInstance(), "BungeeCord");
        RedrockPlugin.getInstance().getServer().getMessenger().registerIncomingPluginChannel((Plugin) RedrockPlugin.getInstance(), "BungeeCord", new ChannelListener());
    }
}
