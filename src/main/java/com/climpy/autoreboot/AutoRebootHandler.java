package com.climpy.autoreboot;

import com.climpy.RedrockPlugin;
import com.climpy.autoreboot.commands.AutoRebootCancelCommand;
import com.climpy.autoreboot.commands.AutoRebootCommand;
import com.climpy.autoreboot.listeners.AutoRebootListener;
import com.climpy.autoreboot.tasks.ServerRebootTask;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class AutoRebootHandler {
    private static List<Integer> rebootTimes;
    private static boolean initiated;
    private static ServerRebootTask serverRebootTask;

    private AutoRebootHandler() {
    }

    public static void init() {
        Preconditions.checkState(!AutoRebootHandler.initiated);
        AutoRebootHandler.initiated = true;
        RedrockPlugin.getInstance().getCommand("autoreboot").setExecutor(new AutoRebootCommand());
        RedrockPlugin.getInstance().getCommand("autorebootcancel").setExecutor(new AutoRebootCancelCommand());

        AutoRebootHandler.rebootTimes = (List<Integer>) ImmutableList.copyOf((Collection) RedrockPlugin.getInstance().getConfig().getIntegerList("AutoRebootTimes"));
        RedrockPlugin.getInstance().getServer().getPluginManager().registerEvents((Listener) new AutoRebootListener(), (Plugin) RedrockPlugin.getInstance());
    }

    @Deprecated
    public static void rebootServer(final int seconds) {
        rebootServer(seconds, TimeUnit.SECONDS);
    }

    public static void rebootServer(final int timeUnitAmount, final TimeUnit timeUnit) {
        if (AutoRebootHandler.serverRebootTask != null) {
            throw new IllegalStateException("Hala devam etmekte olan bir yeniden başlatma var.");
        }
        (AutoRebootHandler.serverRebootTask = new ServerRebootTask(timeUnitAmount, timeUnit)).runTaskTimer((Plugin) RedrockPlugin.getInstance(), 0, 20);
    }

    public static boolean isRebooting() {
        return AutoRebootHandler.serverRebootTask != null;
    }

    public static int getRebootSecondsRemaining() {
        if (AutoRebootHandler.serverRebootTask == null) {
            return -1;
        }
        return AutoRebootHandler.serverRebootTask.getSecondsRemaining();
    }

    public static void cancelReboot() {
        if (AutoRebootHandler.serverRebootTask != null) {
            AutoRebootHandler.serverRebootTask.cancel();
            AutoRebootHandler.serverRebootTask = null;
        }
    }

    public static List<Integer> getRebootTimes() {
        return AutoRebootHandler.rebootTimes;
    }

    public static boolean isInitiated() {
        return AutoRebootHandler.initiated;
    }

    static {
        AutoRebootHandler.initiated = false;
        AutoRebootHandler.serverRebootTask = null;
    }
}
