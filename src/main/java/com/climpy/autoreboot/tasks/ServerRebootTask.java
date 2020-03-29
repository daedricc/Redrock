package com.climpy.autoreboot.tasks;

import com.climpy.RedrockPlugin;
import com.climpy.server.provider.DefaultServerProvider;
import com.climpy.utils.C;
import com.climpy.utils.MiscUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public final class ServerRebootTask extends BukkitRunnable {
    private int secondsRemaining;
    private boolean wasWhitelisted;

    public ServerRebootTask(final int timeUnitAmount, final TimeUnit timeUnit) {
        this.secondsRemaining = (int) timeUnit.toSeconds(timeUnitAmount);
        this.wasWhitelisted = RedrockPlugin.getInstance().getServer().hasWhitelist();
    }

    DefaultServerProvider defaultServerProvider = new DefaultServerProvider();

    public void run() {
        if (this.secondsRemaining == 0) {
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                p.kickPlayer(C.GREEN + "Sunucu yeniden başlatılıyor.");
            }

            RedrockPlugin.getInstance().getServer().reload();
        }

        switch (this.secondsRemaining) {
            case 5:
            case 10:
            case 15:
            case 30:
            case 60:
            case 120:
            case 180:
            case 240:
            case 300: {
                RedrockPlugin.getInstance().getServer().broadcastMessage(ChatColor.RED + "\u26a0 " + ChatColor.DARK_RED.toString() + ChatColor.STRIKETHROUGH + "------------------------" + ChatColor.RED + " \u26a0");
                RedrockPlugin.getInstance().getServer().broadcastMessage(ChatColor.RED + MiscUtils.formatIntoDetailedString(this.secondsRemaining) + " içinde sunucu yeniden başlatılıyor.");
                RedrockPlugin.getInstance().getServer().broadcastMessage(ChatColor.RED + "\u26a0 " + ChatColor.DARK_RED.toString() + ChatColor.STRIKETHROUGH + "------------------------" + ChatColor.RED + " \u26a0");
                break;
            }
        }
        --this.secondsRemaining;
    }

    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        Bukkit.setWhitelist(this.wasWhitelisted);
    }

    public int getSecondsRemaining() {
        return this.secondsRemaining;
    }
}
