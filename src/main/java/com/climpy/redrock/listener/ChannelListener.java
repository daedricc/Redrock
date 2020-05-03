package com.climpy.redrock.listener;

import com.climpy.redrock.RedrockPlugin;
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
        Validate.notNull(uniqueUUID, "Giris bos olamaz!");
        Validate.notNull(reason, "Sebep bos olamaz!");
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

    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
    }


}
