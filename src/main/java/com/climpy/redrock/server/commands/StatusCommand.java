package com.climpy.redrock.server.commands;

import com.climpy.redrock.RedrockPlugin;
import com.climpy.redrock.commands.RedrockCommand;
import com.climpy.redrock.commands.RedrockResultCommand;
import com.climpy.redrock.server.provider.DefaultServerProvider;
import com.climpy.torch.TorchPlugin;
import com.climpy.torch.profile.Profile;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StatusCommand extends RedrockCommand {
    public StatusCommand() {
        super("status", "Sunucu durumunu gösterir.");
    }

    @Override
    public RedrockResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        Profile profile = TorchPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        if(!profile.toPlayer().hasPermission("redrock.server.status") || (!profile.toPlayer().hasPermission("redrock.server.*"))) {
            return RedrockResultCommand.INVALID_PERMISSION;
        }

        String parentFile = RedrockPlugin.getInstance().getDataFolder().getAbsoluteFile().getParentFile().getAbsoluteFile().getParent();
        Runtime serverRuntime = Runtime.getRuntime();
        DefaultServerProvider defaultServerProvider = new DefaultServerProvider();
        File logFile = new File(parentFile + "/logs");
        double spigotTPS = defaultServerProvider.getServerTPSStatus();
        double serverLag = Math.round((1.0D - spigotTPS / 20.0D) * 100.0D);
        ChatColor chatColorType = tpsColor(spigotTPS);
        sender.sendMessage(ChatColor.WHITE + "");
        sender.sendMessage("(T" + defaultServerProvider.getServerType().getDisplayName() + ", ID: " + defaultServerProvider.getServerID() + ")");
        sender.sendMessage(chatColorType + "- Zaman Durumu");
        sender.sendMessage(chatColorType + "Sunucu TPS oranı: " + ChatColor.WHITE + (Math.round(spigotTPS * 10000.0D) / 10000.0D) + "%");
        sender.sendMessage(chatColorType + "Sunucu gecikmesi " + ChatColor.WHITE + (Math.round(serverLag * 10000.0D) / 10000.0D) + "%");
        sender.sendMessage(chatColorType + "şu an ki zaman dilimi: " + ChatColor.WHITE + (new SimpleDateFormat("kk:mm dd-MM-yyyy")).format(new Date()));
        sender.sendMessage("");
        sender.sendMessage(chatColorType + "- Sunucu Durumu");
        sender.sendMessage(chatColorType + "Sunucu Network Adı: " + ChatColor.WHITE + defaultServerProvider.getServerName());
        sender.sendMessage(chatColorType + "Sunucu Türü: " + ChatColor.WHITE + defaultServerProvider.getServerType().getDisplayName());
        sender.sendMessage(chatColorType + "Sunucu IP Adresi: " + ChatColor.WHITE + defaultServerProvider.getServerIP());
        sender.sendMessage(chatColorType + "Sunucu Bukkit Sürümü: " + ChatColor.WHITE + Bukkit.getServer().getBukkitVersion());
        sender.sendMessage((new SimpleDateFormat("kk:mm dd-MM-yyyy")).format(new Date(defaultServerProvider.getServerStartTime())));
        sender.sendMessage("");
        sender.sendMessage(chatColorType + "- Bellek Durumu");
        sender.sendMessage(chatColorType + "Kullanılan: " + ChatColor.WHITE + serverRuntime.availableProcessors());
        sender.sendMessage(chatColorType + "Maksimum bellek: " + ChatColor.WHITE + (serverRuntime.maxMemory() / 1048576L) + "MB");
        sender.sendMessage(chatColorType + "Toplam Hafıza: " + ChatColor.WHITE + (serverRuntime.totalMemory() / 1048576L) + "MB");
        sender.sendMessage(chatColorType + "Boş Hafıza: " + ChatColor.WHITE + (serverRuntime.freeMemory() / 1048576L) + "MB");
        sender.sendMessage("");
        sender.sendMessage(chatColorType + "- Disk Durumu");
        sender.sendMessage(chatColorType + "Sunucu günlük boyutu: " + ChatColor.GOLD + logFile.length() + " (" +
                Double.valueOf((new DecimalFormat("###.##")).format(logFile.length() / 1048576L)) + " MB)");
        sender.sendMessage(chatColorType + "Boş günlük: " + ChatColor.GOLD + logFile.getFreeSpace() + " (" +
                Double.valueOf((new DecimalFormat("###.##")).format(logFile.getFreeSpace() / 1048576L)) + " MB)");
        for (World world : Bukkit.getWorlds()) {
            World.Environment worldEnvironment = world.getEnvironment();
            String environmentName = WordUtils.capitalizeFully(worldEnvironment.name().replace('_', ' '));
            int tileEntities = 0;
            Chunk[] loadedChunks = world.getLoadedChunks();
            for (Chunk chunk : loadedChunks)
                tileEntities += (chunk.getTileEntities()).length;
            sender.sendMessage(ChatColor.RED + world.getName() + '(' + environmentName + "): " + ChatColor.YELLOW + loadedChunks.length + " bölge, " + world
                    .getEntities().size() + " kişi, " + tileEntities + " adet varlık.");
        }
        sender.sendMessage(ChatColor.GRAY + "Sunucu açılışı: " + (new SimpleDateFormat("kk:mm dd-MM-yyyy")).format(new Date(defaultServerProvider.getServerStartTime())));
        sender.sendMessage("" + defaultServerProvider.getServerWhitelist());
        return RedrockResultCommand.SUCCESS;
    }

    private static ChatColor tpsColor(double spigotTPS) {
        ChatColor chatColorType;
        if (spigotTPS >= 18.0D) {
            chatColorType = ChatColor.DARK_GREEN;
        } else if (spigotTPS >= 15.0D) {
            chatColorType = ChatColor.YELLOW;
        } else {
            chatColorType = ChatColor.RED;
        }
        return chatColorType;
    }
}
