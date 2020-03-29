package com.climpy.command.commands.core;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.redis.data.DataPublisher;
import com.climpy.utils.C;
import com.climpy.utils.MiscUtils;
import com.google.gson.JsonObject;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StreamingCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        if (sender instanceof Player) {
            Player targetPlayer;
            Profile user = RedrockPlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());
            if (!user.getRankType().isAboveOrEqual(RankType.FAMOUS)) {
                sender.sendMessage(Locale.format(Locale.NO_PERM));
                return true;
            }

            if (args.length < 1) {
                player.sendMessage(C.RED + "Kullanım: /" + label + " <yayınLinki..>");
                return true;
            }

            String streamLink = MiscUtils.joinStrings(args, 0);

            DataPublisher dataPublisher = RedrockPlugin.getInstance().getRedisConnection().getDataPublisher();
            JsonObject streamObject = new JsonObject();
            streamObject.addProperty("uuid", user.getUniqueUUID().toString());
            streamObject.addProperty("stream_link", streamLink);
            dataPublisher.write("streaming", streamObject);
            return true;
        }

        sender.sendMessage("Konsoldan kullanamazsin.");
        return true;
    }
}
