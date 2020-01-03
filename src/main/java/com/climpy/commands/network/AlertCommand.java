package com.climpy.commands.network;

import com.climpy.RedrockPlugin;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.profile.utils.MiscUtils;
import com.climpy.redis.data.DataPublisher;
import com.climpy.utils.C;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AlertCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getName());
        if(!user.getRankType().isAboveOrEqual(RankType.MOD)) {
            player.sendMessage(C.RED + "Bu işlem için " + RankType.MOD.getDisplayName() + C.RED + " ve üzeri yetki gereklidir.");
            return true;
        }

        if(args.length < 1) {
            player.sendMessage(C.RED + "Duyuru yayımlamak için mesaj yazın.");
            return true;
        }

        String message = MiscUtils.joinStrings(args, 0);
        if (message.length() < 4 && !message.isEmpty()) {
            sender.sendMessage(C.RED + "Girilen mesaj en az 3 karakter olmalıdır.");
            return true;
        }

        DataPublisher dataPublisher = RedrockPlugin.getInstance().getRedisConnection().getDataPublisher();
        JsonObject alertObject = new JsonObject();
        alertObject.addProperty("uuid", user.getUniqueUUID().toString());
        alertObject.addProperty("message", message);
        dataPublisher.write("alert_global", alertObject);
        sender.sendMessage(C.GREEN + "Duyuru yayımlandı.");
        return true;
    }
}
