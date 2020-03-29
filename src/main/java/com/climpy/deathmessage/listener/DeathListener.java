package com.climpy.deathmessage.listener;

import com.climpy.RedrockPlugin;
import com.climpy.deathmessage.DeathMessageConfiguration;
import com.climpy.deathmessage.RedrockDeathMessageHandler;
import com.climpy.deathmessage.damage.Damage;
import com.climpy.deathmessage.damage.PlayerDamage;
import com.climpy.deathmessage.damage.UnknownDamage;
import net.minecraft.server.v1_8_R3.EntityHuman;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class DeathListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeathEarly(final PlayerDeathEvent event) {
        final List<Damage> record = RedrockDeathMessageHandler.getDamage(event.getEntity());
        if (!record.isEmpty()) {
            final Damage deathCause = record.get(record.size() - 1);
            if (deathCause instanceof PlayerDamage && deathCause.getTimeAgoMillis() < TimeUnit.MINUTES.toMillis(1L)) {
                final Player killerPlayer = ((PlayerDamage) deathCause).getDamager();
                if (killerPlayer != null) {
                    ((CraftPlayer) event.getEntity()).getHandle().killer = (EntityHuman) ((CraftPlayer) killerPlayer).getHandle();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeathLate(final PlayerDeathEvent event) {
        final List<Damage> record = RedrockDeathMessageHandler.getDamage(event.getEntity());
        Damage deathCause;
        if (!record.isEmpty()) {
            deathCause = record.get(record.size() - 1);
        } else {
            deathCause = new UnknownDamage(event.getEntity(), 1.0);
        }
        RedrockDeathMessageHandler.clearDamage(event.getEntity());
        event.setDeathMessage((String) null);
        final DeathMessageConfiguration configuration = RedrockDeathMessageHandler.getConfiguration();
        final UUID diedUuid = event.getEntity().getUniqueId();
        final UUID killerUuid = (event.getEntity().getKiller() == null) ? null : event.getEntity().getKiller().getUniqueId();
        for (final Player player : RedrockPlugin.getInstance().getServer().getOnlinePlayers()) {
            final boolean showDeathMessage = configuration.shouldShowDeathMessage(player.getUniqueId(), diedUuid, killerUuid);
            if (showDeathMessage) {
                final String deathMessage = deathCause.getDeathMessage(player);
                player.sendMessage(deathMessage);
            }
        }
    }
}
