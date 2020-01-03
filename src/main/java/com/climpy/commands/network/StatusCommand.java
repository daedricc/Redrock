package com.climpy.commands.network;

import com.climpy.RedrockPlugin;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.server.provider.DefaultServerProvider;
import com.climpy.utils.DateTimeUtils;
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

public class StatusCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());
        if (!user.getRankType().isAboveOrEqual(RankType.CHAT_MOD)) {
            sender.sendMessage(ChatColor.RED + "Bunun için" + RankType.CHAT_MOD.getColor() + RankType.CHAT_MOD
                    .getDisplayName() + ChatColor.RED + " ve üzeri gereklidir.");
            return true;
        }

        String parentFile = RedrockPlugin.getInstance().getDataFolder().getAbsoluteFile().getParentFile().getAbsoluteFile().getParent();
        Runtime serverRuntime = Runtime.getRuntime();
        DefaultServerProvider defaultServerProvider = new DefaultServerProvider();
        File logFile = new File(parentFile + "/logs");
        double spigotTPS = defaultServerProvider.getServerTPSStatus();
        double serverLag = Math.round((1.0D - spigotTPS / 20.0D) * 100.0D);
        ChatColor chatColorType = RedrockPlugin.tpsColor(spigotTPS);
        sender.sendMessage(ChatColor.WHITE + "");
        sender.sendMessage("(T" + defaultServerProvider.getServerType().getDisplayName() + ", ID: " + defaultServerProvider.getServerID() + ")");
        sender.sendMessage(chatColorType + "- Zaman Durumu");
                sender.sendMessage(chatColorType + "Sunucu TPS oranı: " + ChatColor.WHITE + (Math.round(spigotTPS * 10000.0D) / 10000.0D) + "%");
        sender.sendMessage(chatColorType + "Sunucu gecikmes " + ChatColor.WHITE + (Math.round(serverLag * 10000.0D) / 10000.0D) + "%");
        sender.sendMessage(chatColorType + "şu an ki zaman dilimi: " + ChatColor.WHITE + (new SimpleDateFormat("kk:mm dd-MM-yyyy")).format(new Date()));
        sender.sendMessage("");
        sender.sendMessage(chatColorType + "- Sunucu Durumu");
                sender.sendMessage(chatColorType + "Sunucu Network Adı: " + ChatColor.WHITE + defaultServerProvider.getServerName());
        sender.sendMessage(chatColorType + "Sunucu Türü: " + ChatColor.WHITE + defaultServerProvider.getServerType().getDisplayName());
        sender.sendMessage(chatColorType + "Sunucu IP Adresi: " + ChatColor.WHITE + defaultServerProvider.getServerIP());
        sender.sendMessage(chatColorType + "Sunucu Bukkit Sürümü: " + ChatColor.WHITE + Bukkit.getServer().getBukkitVersion());
        if (sender instanceof Player) {
            Player player = Bukkit.getPlayer(sender.getName());
            if (!player.isOp() || !user.getRankType().isAboveOrEqual(RankType.ADMIN)) {
                sender.sendMessage((new SimpleDateFormat("kk:mm dd-MM-yyyy")).format(new Date(defaultServerProvider.getServerStartTime())));
                return true;
            }
        }
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
        return true;
    }
}
