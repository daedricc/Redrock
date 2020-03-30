package com.climpy.command.extnds.commands.bukkit.teleport;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportPositionCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        Profile profile = RedrockPlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if(!profile.toPlayer().hasPermission("redrock.moderator")) {
            sender.sendMessage(Locale.format(Locale.NO_PERM));
            return true;
        }

        if (args.length != 3) {
            sender.sendMessage(C.RED + "Kullanım: /" + label + " [x] [y] [z]");
            return true;
        }


        if (isDouble(args[0]) || isDouble(args[1]) || isDouble(args[2])) {
            sender.sendMessage(C.RED + "Kullanım: /" + label + " [x] [y] [z]");
            return true;
        }

        double x = Double.parseDouble(args[0]) + 0.5;
        double y = Double.parseDouble(args[1]) + 0.5;
        double z = Double.parseDouble(args[2]) + 0.5;
        Location loc = new Location(player.getWorld(), x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch());
        player.teleport(loc);
        player.sendMessage(C.GREEN + "Belirtilen kordinatlara ışınlandınız. §7(§a" + x + "§d, §a" + y + "§d, §a" + z + "§7)");
        return true;
    }

    public boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
