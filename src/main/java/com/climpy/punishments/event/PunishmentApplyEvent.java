package com.climpy.punishments.event;

import com.climpy.punishments.Punishment;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

@Getter
public class PunishmentApplyEvent extends Event implements Cancellable {
    private static HandlerList handlerList = new HandlerList();
    private boolean cancelled = false;

    private List<Punishment> altPunishments;
    private Punishment punishment;

    private boolean announce, silent;

    public PunishmentApplyEvent(Punishment punishment, List<Punishment> altPunishments, boolean announce, boolean silent) {
        this.altPunishments = altPunishments;
        this.punishment = punishment;

        this.announce = announce;
        this.silent = silent;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
