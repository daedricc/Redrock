package com.climpy.command.network.players;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.rank.RankType;
import com.climpy.redis.data.DataPublisher;
import com.climpy.server.provider.DefaultServerProvider;
import com.climpy.utils.C;
import com.climpy.utils.CooldownUtils;
import com.climpy.utils.MiscUtils;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RequestCommand implements CommandExecutor {

    public RequestCommand() {
        CooldownUtils.createCooldown("Request");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        if (args.length < 1) {
            sender.sendMessage(C.RED + "Kullanım: /" + label + " <mesaj>");
            return true;
        }

        if (CooldownUtils.isOnCooldown("Request", (Player) sender)) {
            sender.sendMessage(C.translate("&d&l" + CooldownUtils.getCooldownForPlayerInt("Report", (Player) sender)) + " &dsaniye sonra istek gönderebilirsiniz.");
            return true;
        }

        DefaultServerProvider defaultServerProvider = new DefaultServerProvider();
        String message = MiscUtils.joinStrings(args, 0);
        String serverName = RedrockPlugin.getInstance().getServerName();

        int serverID = RedrockPlugin.getInstance().getServerID();

        Profile requestedUser = RedrockPlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (!requestedUser.getRankType().isAboveOrEqual(RankType.CHAT_MOD))
            sender.sendMessage(C.GREEN + "İstek gönderildi, en yakın zamanda bakılacak." + "\n" + C.DARK_GREEN + "Daha hızlı çözüm için: " + C.YELLOW + "discord.gg/moosy");
        DataPublisher dataPublisher = RedrockPlugin.getInstance().getRedisConnection().getDataPublisher();
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("uuid", requestedUser.getUniqueUUID().toString());
        requestObject.addProperty("serverName", defaultServerProvider.getServerName());
        requestObject.addProperty("message", message);
        dataPublisher.write("request_alerts", requestObject);
        return true;
    }
}
