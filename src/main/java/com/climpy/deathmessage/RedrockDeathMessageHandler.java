package com.climpy.deathmessage;

import com.climpy.RedrockPlugin;
import com.climpy.deathmessage.damage.Damage;
import com.climpy.deathmessage.listener.DamageListener;
import com.climpy.deathmessage.listener.DeathListener;
import com.climpy.deathmessage.listener.DisconnectListener;
import com.climpy.deathmessage.tracker.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.*;

public final class RedrockDeathMessageHandler {
    private static DeathMessageConfiguration configuration;
    private static Map<UUID, List<Damage>> damage;
    private static boolean initiated;

    private RedrockDeathMessageHandler() {
    }

    public static void init() {
        Preconditions.checkState(!RedrockDeathMessageHandler.initiated);
        RedrockDeathMessageHandler.initiated = true;
        final PluginManager pluginManager = RedrockPlugin.getInstance().getServer().getPluginManager();
        pluginManager.registerEvents((Listener) new DamageListener(), (Plugin) RedrockPlugin.getInstance());
        pluginManager.registerEvents((Listener) new DeathListener(), (Plugin) RedrockPlugin.getInstance());
        pluginManager.registerEvents((Listener) new DisconnectListener(), (Plugin) RedrockPlugin.getInstance());
        pluginManager.registerEvents((Listener) new GeneralTracker(), (Plugin) RedrockPlugin.getInstance());
        pluginManager.registerEvents((Listener) new PvPTracker(), (Plugin) RedrockPlugin.getInstance());
        pluginManager.registerEvents((Listener) new EntityTracker(), (Plugin) RedrockPlugin.getInstance());
        pluginManager.registerEvents((Listener) new FallTracker(), (Plugin) RedrockPlugin.getInstance());
        pluginManager.registerEvents((Listener) new ArrowTracker(), (Plugin) RedrockPlugin.getInstance());
        pluginManager.registerEvents((Listener) new VoidTracker(), (Plugin) RedrockPlugin.getInstance());
        pluginManager.registerEvents((Listener) new BurnTracker(), (Plugin) RedrockPlugin.getInstance());
    }

    public static List<Damage> getDamage(final Player player) {
        return (List<Damage>) (RedrockDeathMessageHandler.damage.containsKey(player.getUniqueId()) ? ((List<Damage>) RedrockDeathMessageHandler.damage.get(player.getUniqueId())) : ImmutableList.of());
    }

    public static void addDamage(final Player player, final Damage addedDamage) {
        RedrockDeathMessageHandler.damage.putIfAbsent(player.getUniqueId(), new ArrayList<Damage>());
        final List<Damage> damageList = RedrockDeathMessageHandler.damage.get(player.getUniqueId());
        while (damageList.size() > 30) {
            damageList.remove(0);
        }
        damageList.add(addedDamage);
    }

    public static void clearDamage(final Player player) {
        RedrockDeathMessageHandler.damage.remove(player.getUniqueId());
    }

    public static DeathMessageConfiguration getConfiguration() {
        return RedrockDeathMessageHandler.configuration;
    }

    public static void setConfiguration(final DeathMessageConfiguration configuration) {
        RedrockDeathMessageHandler.configuration = configuration;
    }

    static {
        RedrockDeathMessageHandler.configuration = DeathMessageConfiguration.DEFAULT_CONFIGURATION;
        RedrockDeathMessageHandler.damage = new HashMap<UUID, List<Damage>>();
        RedrockDeathMessageHandler.initiated = false;
    }
}
