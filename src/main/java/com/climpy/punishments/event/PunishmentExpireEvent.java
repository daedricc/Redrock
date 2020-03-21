package com.climpy.punishments.event;

import com.climpy.punishments.Punishment;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class PunishmentExpireEvent extends Event implements Cancellable {
    private static HandlerList handlerList = new HandlerList();
    private boolean cancelled = false;

    private Punishment punishment;

    public PunishmentExpireEvent(Punishment punishment) {
        this.punishment = punishment;
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
