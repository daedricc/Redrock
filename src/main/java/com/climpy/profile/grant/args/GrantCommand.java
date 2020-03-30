package com.climpy.profile.grant.args;

import com.climpy.RedrockPlugin;
import com.climpy.command.RedrockCommand;
import com.climpy.command.RedrockResultCommand;
import com.climpy.profile.Profile;
import com.climpy.profile.ProfileManager;
import com.climpy.profile.grant.Grant;
import com.climpy.profile.grant.event.GrantAppliedEvent;
import com.climpy.profile.rank.Rank;
import com.climpy.redis.data.DataPublisher;
import com.climpy.utils.C;
import com.climpy.utils.DurationUtil;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GrantCommand extends RedrockCommand {
    public GrantCommand() {
        super("grant", "Kullanıcılara yetki verir.");
    }

    @Override
    public RedrockResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        if (!hasPermission(sender, "cookie.command.grant"))
            return RedrockResultCommand.INVALID_PERMISSION;

        if (args.length < 4) {
            setUsage(String.valueOf(label) + " <player> <rank> <duration> <reason>");
            return RedrockResultCommand.INVALID_USAGE;
        }

        String profileName = args[0];
        ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
        Profile profile = profileManager.getProfile(profileName);
        if (profile == null || !profile.isLoaded()) {
            setPlayer(profileName);
            return RedrockResultCommand.INVALID_PLAYER;
        }

        String rankName = args[1];
        Rank rank = Rank.getRankByDisplayName(rankName);
        if (rank == null) {
            sender.sendMessage(C.RED + "Bu isimde bir rütbe bulunamadı.");
            return RedrockResultCommand.SUCCESS;
        }

        String durationTime = args[2];
        DurationUtil duration = DurationUtil.fromString(durationTime);
        if (duration.getValue() == -1L) {
            sender.sendMessage(C.RED + "Girilen süre geçersiz.");
            sender.sendMessage(C.RED + "Süreler: 1d2h (1 gün, and 2 saat) / perm - permanent");
            return RedrockResultCommand.SUCCESS;
        }

        UUID addedBy = (sender instanceof Player) ? ((Player)sender).getUniqueId() : null;
        Grant grant = new Grant(UUID.randomUUID(), rank, addedBy, System.currentTimeMillis(), getMessage(args), duration.getValue());
        profile.getGrants().add(grant);
        profile.activateNextGrant();

       // NightPlugin.getPlugin().getDatabaseManager().getRedisImpl().getRedis().sendPacket((Packet)new PacketAddGrant(profile.getUuid(), grant));

        DataPublisher dataPublisher = RedrockPlugin.getInstance().getRedisConnection().getDataPublisher();
        JsonObject streamObject = new JsonObject();
        streamObject.addProperty("uuid", profile.getUniqueUUID().toString());
        dataPublisher.write("add_grant", streamObject);

       // ChatUtil.sendMessage(commandsender, new String[] { "&eYou applied a &r" + rank.getColoredName() + "&e grant to &r" + profile.getColoredUsername() });
        sender.sendMessage("" + rank.getColoredName() + " yetkisini " + profile.getName() + " adlı oyuncuya verdin.");

        Player player = profile.toPlayer();
        if (player != null) {
            Bukkit.getPluginManager().callEvent(new GrantAppliedEvent(player, grant));
         //   profile.refreshDisplayName();
            player.kickPlayer(C.translate("&cYetkin yükseltildi " + rank.getDisplayName() + ", sunucuya yeniden gir."));
        }
        profile.saveProfile();
        return RedrockResultCommand.SUCCESS;
    }

    private String getMessage(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 3; i < args.length; i++)
            sb.append(args[i]).append(" ");
        return sb.toString();
    }
}
