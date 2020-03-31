package com.climpy.profile.grant.args;

import com.climpy.Locale;
import com.climpy.RedrockPlugin;
import com.climpy.command.RedrockCommand;
import com.climpy.command.RedrockResultCommand;
import com.climpy.profile.Profile;
import com.climpy.profile.grant.Grant;
import com.climpy.profile.grant.menu.GrantsMenu;
import com.climpy.punishments.Punishment;
import com.climpy.punishments.history.menu.HistoryCategoryMenu;
import com.climpy.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GrantsCommand extends RedrockCommand {
    public GrantsCommand() {
        super("grants", "Grantlar");
    }

    @Override
    public RedrockResultCommand execute(CommandSender sender, Command command, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        Profile targetProfile = RedrockPlugin.getInstance().getProfileManager().getProfile(args[0]);
        if (targetProfile == null) {
            setPlayer(args[0]);
            return RedrockResultCommand.INVALID_PLAYER;
        }

        List<Grant> grants = targetProfile.getGrants();
        if (grants.isEmpty()) {
            sender.sendMessage(C.RED + "Bu kullanıcının rütbe geçmişi bulunamadı.");
            return RedrockResultCommand.SUCCESS;
        }

        GrantsMenu.getInventory(targetProfile).open(player);
        return RedrockResultCommand.SUCCESS;
    }
}
