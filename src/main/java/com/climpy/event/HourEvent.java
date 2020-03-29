

package com.climpy.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.beans.ConstructorProperties;

public class HourEvent extends Event {
    private static final HandlerList handlerList;
    private int hour;

    public HandlerList getHandlers() {
        return HourEvent.handlerList;
    }

    @ConstructorProperties({"hour"})
    public HourEvent(final int hour) {
        this.hour = hour;
    }

    public static HandlerList getHandlerList() {
        return HourEvent.handlerList;
    }

    public int getHour() {
        return this.hour;
    }

    static {
        handlerList = new HandlerList();
    }
}
