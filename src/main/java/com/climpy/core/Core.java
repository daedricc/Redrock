package com.climpy.core;

import com.climpy.RedrockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Core {
    public static void setSpawnLocation(Player player) {
        RedrockPlugin.getInstance().getConfig().set("spawn.world", player.getLocation().getWorld().getName());
        RedrockPlugin.getInstance().getConfig().set("spawn.x", Double.valueOf(player.getLocation().getX()));
        RedrockPlugin.getInstance().getConfig().set("spawn.y", Double.valueOf(player.getLocation().getY()));
        RedrockPlugin.getInstance().getConfig().set("spawn.z", Double.valueOf(player.getLocation().getZ()));
        RedrockPlugin.getInstance().getConfig().set("spawn.yaw", Float.valueOf(player.getLocation().getYaw()));
        RedrockPlugin.getInstance().getConfig().set("spawn.pitch", Float.valueOf(player.getLocation().getPitch()));
        RedrockPlugin.getInstance().saveConfig();
    }

    public static void teleportToSpawn(Player player) {
        World w = Bukkit.getServer().getWorld(RedrockPlugin.getInstance().getConfig().getString("spawn.world"));
        double x = RedrockPlugin.getInstance().getConfig().getDouble("spawn.x");
        double y = RedrockPlugin.getInstance().getConfig().getDouble("spawn.y");
        double z = RedrockPlugin.getInstance().getConfig().getDouble("spawn.z");
        float yaw = (float) RedrockPlugin.getInstance().getConfig().getDouble("spawn.yaw");
        float pitch = (float) RedrockPlugin.getInstance().getConfig().getDouble("spawn.pitch");
        player.teleport(new Location(w, x, y, z, yaw, pitch));
    }
}
