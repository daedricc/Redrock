package com.climpy.redrock.commands.network.players;

import com.climpy.redrock.RedrockPlugin;
import com.climpy.redrock.commands.RedrockCommand;
import com.climpy.redrock.commands.RedrockResultCommand;
import com.climpy.redrock.redis.data.DataPublisher;
import com.climpy.redrock.server.provider.DefaultServerProvider;
import com.climpy.torch.TorchPlugin;
import com.climpy.torch.profile.Profile;
import com.climpy.redrock.utils.C;
import com.climpy.redrock.utils.CooldownUtils;
import com.climpy.redrock.utils.MiscUtils;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RequestCommand extends RedrockCommand {

    public RequestCommand() {
        super("request");
        CooldownUtils.createCooldown("Request");
    }

    @Override
    public RedrockResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        if (args.length < 1) {
            sender.sendMessage(C.RED + "Kullanım: /" + label + " <mesaj>");
             return RedrockResultCommand.SUCCESS;
        }

        if (CooldownUtils.isOnCooldown("Request", (Player) sender)) {
            sender.sendMessage(C.translate("&d&l" + CooldownUtils.getCooldownForPlayerInt("Report", (Player) sender)) + " &dsaniye sonra istek gönderebilirsiniz.");
             return RedrockResultCommand.SUCCESS;
        }

        DefaultServerProvider defaultServerProvider = new DefaultServerProvider();
        String message = MiscUtils.joinStrings(args, 0);
        String serverName = RedrockPlugin.getInstance().getServerName();

        int serverID = RedrockPlugin.getInstance().getServerID();

        Profile requestedUser = TorchPlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (!requestedUser.getRank().hasPermission("torch.staff"))
            sender.sendMessage(C.GREEN + "İstek gönderildi, en yakın zamanda bakılacak." + "\n" + C.DARK_GREEN + "Daha hızlı çözüm için: " + C.YELLOW + "discord.gg/moosy");
        DataPublisher dataPublisher = RedrockPlugin.getInstance().getRedisConnection().getDataPublisher();
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("uuid", requestedUser.getUniqueUUID().toString());
        requestObject.addProperty("serverName", defaultServerProvider.getServerName());
        requestObject.addProperty("message", message);
        dataPublisher.write("request_alerts", requestObject);
         return RedrockResultCommand.SUCCESS;
    }
}
