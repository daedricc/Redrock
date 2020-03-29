package com.climpy.deathmessage;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface DeathMessageConfiguration {
    public static final DeathMessageConfiguration DEFAULT_CONFIGURATION = new DeathMessageConfiguration() {
        @Override
        public boolean shouldShowDeathMessage(final UUID checkFor, final UUID died, final UUID killer) {
            return true;
        }

        @Override
        public String formatPlayerName(final Player player) {
            return ChatColor.RED + player.getName();
        }
    };

    boolean shouldShowDeathMessage(final UUID p0, final UUID p1, final UUID p2);

    String formatPlayerName(final Player p0);

    default String formatPlayerName(final Player player, final Player formatFor) {
        return this.formatPlayerName(player);
    }

    default boolean hideWeapons() {
        return false;
    }
}
