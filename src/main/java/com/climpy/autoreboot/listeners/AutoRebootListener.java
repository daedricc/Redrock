package com.climpy.autoreboot.listeners;

import com.climpy.autoreboot.AutoRebootHandler;
import com.climpy.event.HourEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.concurrent.TimeUnit;

public class AutoRebootListener implements Listener {
    @EventHandler
    public void onHour(final HourEvent event) {
        if (AutoRebootHandler.getRebootTimes().contains(event.getHour())) {
            AutoRebootHandler.rebootServer(5, TimeUnit.MINUTES);
        }
    }
}
