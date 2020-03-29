package com.climpy.deathmessage.damage;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class UnknownDamage extends Damage {
    public UnknownDamage(final Player damaged, final double damage) {
        super(damaged, damage);
    }

    @Override
    public String getDeathMessage(final Player getFor) {
        return Damage.wrapName(this.getDamaged(), getFor) + ChatColor.YELLOW + " öldü.";
    }
}
