

package com.climpy.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.beans.ConstructorProperties;

public class HalfHourEvent extends Event {
    private static final HandlerList handlerList;
    private int hour;
    private int minute;

    public HandlerList getHandlers() {
        return HalfHourEvent.handlerList;
    }

    @ConstructorProperties({"hour", "minute"})
    public HalfHourEvent(final int hour, final int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public static HandlerList getHandlerList() {
        return HalfHourEvent.handlerList;
    }

    public int getHour() {
        return this.hour;
    }

    public int getMinute() {
        return this.minute;
    }

    static {
        handlerList = new HandlerList();
    }
}
