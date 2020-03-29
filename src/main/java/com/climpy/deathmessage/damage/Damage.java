package com.climpy.deathmessage.damage;

import com.climpy.deathmessage.DeathMessageConfiguration;
import com.climpy.deathmessage.RedrockDeathMessageHandler;
import org.bukkit.entity.Player;

public abstract class Damage {
    private final Player damaged;
    private final double damage;
    private final long time;

    public Damage(final Player damaged, final double damage) {
        this.damaged = damaged;
        this.damage = damage;
        this.time = System.currentTimeMillis();
    }

    public static String wrapName(final Player player, final Player wrapFor) {
        final DeathMessageConfiguration configuration = RedrockDeathMessageHandler.getConfiguration();
        return configuration.formatPlayerName(player, wrapFor);
    }

    public static String wrapName(final Player player) {
        final DeathMessageConfiguration configuration = RedrockDeathMessageHandler.getConfiguration();
        return configuration.formatPlayerName(player);
    }

    public abstract String getDeathMessage(final Player p0);

    public long getTimeAgoMillis() {
        return System.currentTimeMillis() - this.time;
    }

    public Player getDamaged() {
        return this.damaged;
    }

    public double getDamage() {
        return this.damage;
    }

    public long getTime() {
        return this.time;
    }
}
