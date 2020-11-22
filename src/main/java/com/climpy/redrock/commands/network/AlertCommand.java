package com.climpy.redrock.commands.network;

import com.climpy.redrock.RedrockPlugin;
import com.climpy.redrock.commands.RedrockCommand;
import com.climpy.redrock.commands.RedrockResultCommand;
import com.climpy.redrock.redis.data.DataPublisher;
import com.climpy.torch.TorchPlugin;
import com.climpy.torch.profile.Profile;
import com.climpy.redrock.utils.C;
import com.climpy.redrock.utils.MiscUtils;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AlertCommand extends RedrockCommand {
    public AlertCommand() {
        super("alert", "Global broadcast command");
    }

    @Override
    public RedrockResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        Profile user = TorchPlugin.getInstance().getProfileManager().getProfile(player.getName());
        if (!user.getRank().hasPermission("torch.staff")) {
            return RedrockResultCommand.INVALID_PERMISSION;
        }

        if (args.length < 1) {
            player.sendMessage(C.RED + "Duyuru yayımlamak için mesaj yazın.");
            return RedrockResultCommand.SUCCESS;
        }

        String message = MiscUtils.joinStrings(args, 0);
        if (message.length() < 4 && !message.isEmpty()) {
            sender.sendMessage(C.RED + "Girilen mesaj en az 3 karakter olmalıdır.");
            return RedrockResultCommand.SUCCESS;
        }

        DataPublisher dataPublisher = RedrockPlugin.getInstance().getRedisConnection().getDataPublisher();
        JsonObject alertObject = new JsonObject();
        alertObject.addProperty("uuid", user.getUniqueUUID().toString());
        alertObject.addProperty("message", message);
        dataPublisher.write("alert_global", alertObject);
        sender.sendMessage(C.GREEN + "Duyuru yayımlandı.");
        return RedrockResultCommand.SUCCESS;
    }

}
