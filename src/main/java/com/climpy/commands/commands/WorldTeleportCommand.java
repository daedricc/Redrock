package com.climpy.commands.commands;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldTeleportCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());
        Player player = Bukkit.getPlayer(sender.getName());
        if (sender instanceof Player) {
            if (!user.getRankType().isAboveOrEqual(RankType.MOD)) {
                sender.sendMessage(ChatColor.RED + "Bu işlem için " + RankType.MOD.getDisplayName() + ChatColor.RED + " ve üzeri yetki gereklidir.");
                return true;
            }
        }

        if (args.length == 0) {
            player.sendMessage("§cKullanım: /" + label + " <dünya-Adı..>");
            return true;
        }

        String worldName = "";
        for (String arg : args)
            worldName = arg;
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            player.teleport(new Location(world, 0.0D, 80.0D, 0.0D));
            player.sendMessage(ChatColor.WHITE + "\"" + worldName + "\"" + ChatColor.GOLD + " dünyasına ışınlandınız.");
        }
        else if (world == null) {
            player.sendMessage("§cBöyle bir dünya bulunamadı, oluşturmak için;");
            player.sendMessage("§c/createworld " + args[0]);
        }
        return true;
    }
}
