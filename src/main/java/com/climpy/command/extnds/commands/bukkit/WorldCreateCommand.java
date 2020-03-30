package com.climpy.command.extnds.commands.bukkit;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.server.ServerType;
import com.climpy.server.provider.DefaultServerProvider;
import com.climpy.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldCreateCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(sender.getName());
        Player player = Bukkit.getPlayer(sender.getName());
        DefaultServerProvider defaultServerProvider = new DefaultServerProvider();

        if (defaultServerProvider.getServerType() == ServerType.LOBI) {
            sender.sendMessage(C.RED + "Lobi sunucularında bu komutu kullanamazsınız.");
            return true;
        }

        if (sender instanceof Player) {
            if(!user.toPlayer().hasPermission("redrock.moderator")) {
            sender.sendMessage(Locale.format(Locale.NO_PERM));
            return true;
        }
        }

        if (args.length < 1) {
            player.sendMessage(C.RED + "Kullanım: /" + label + " <dünya-Adı..>");
            return true;
        }

        String worldName = "";
        for (String arg : args)
            worldName = arg;
        player.sendMessage(C.translate("&f\"" + worldName + "\"" + " &6isminde sahip bir dünya oluşturuluyor.."));
        World world = Bukkit.createWorld(WorldCreator.name(worldName));
        player.sendMessage(C.translate("&f\"" + worldName + "\"" + " &6ismine sahip dünya oluşturuldu, ışınlanıyorsunuz.."));
        player.teleport(new Location(world, 0.0D, 80.0D, 0.0D));
        return true;
    }
}
