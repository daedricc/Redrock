package com.climpy.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ScoreboardManager implements Listener {

    private final JavaPlugin plugin;
    private final ScoreboardEntrySupplier supplier;
    private boolean registered = true;
    private BukkitTask task;

    private final Map<UUID, PlayerScoreboard> scoreboards;

    public ScoreboardManager(JavaPlugin plugin, ScoreboardEntrySupplier supplier) {
        this.plugin = plugin;
        this.supplier = supplier;
        scoreboards = new ConcurrentHashMap<>();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        Bukkit.getOnlinePlayers().forEach(x -> {
            scoreboards.put(x.getUniqueId(), new PlayerScoreboard(x, supplier));
        });
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            scoreboards.forEach((id, scoreboard) -> scoreboard.send());
        }, 15L, 15L);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        scoreboards.put(player.getUniqueId(), new PlayerScoreboard(player, supplier));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerScoreboard scoreboard = scoreboards.remove(player.getUniqueId());
        if (scoreboard != null) {
            scoreboard.clean();
        }
    }

    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        if (event.getPlugin() == plugin) {
            unregister();
        }
    }

    public void unregister() {
        if (registered) {
            registered = false;
            HandlerList.unregisterAll(this);
            if (task != null && Bukkit.getScheduler().isCurrentlyRunning(task.getTaskId())) {
                task.cancel();
            }
            scoreboards.forEach((id, scoreboard) -> scoreboard.clean());
            scoreboards.clear();
        }
    }

}
