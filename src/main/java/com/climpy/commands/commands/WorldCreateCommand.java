package com.climpy.commands.commands;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.utils.C;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldCreateCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());
        Player player = Bukkit.getPlayer(sender.getName());
        if (sender instanceof Player) {
            if (!user.getRankType().isAboveOrEqual(RankType.MOD)) {
                sender.sendMessage(C.RED + "Bu işlem için " + RankType.MOD.getDisplayName() + C.RED + " ve üzeri yetki gereklidir.");
                return true;
            }
        }

        if(args.length < 1) {
            player.sendMessage(C.RED + "&cKullanım: /" + label + " <dünya-Adı..>");
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
