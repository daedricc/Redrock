package com.climpy.command.extnds.commands.bukkit;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldTeleportCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        Player player = Bukkit.getPlayer(sender.getName());
        if (sender instanceof Player) {
            if(!user.toPlayer().hasPermission("redrock.moderator")) {
            sender.sendMessage(Locale.format(Locale.NO_PERM));
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
            player.sendMessage(C.WHITE + "\"" + worldName + "\"" + C.GOLD + " dünyasına ışınlandınız.");
        } else if (world == null) {
            player.sendMessage("§cBöyle bir dünya bulunamadı, oluşturmak için;");
            player.sendMessage("§c/createworld " + args[0]);
        }
        return true;
    }
}
