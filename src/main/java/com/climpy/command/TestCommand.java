package com.climpy.command;

import com.climpy.RedrockPlugin;
import com.climpy.profile.Profile;
import com.climpy.profile.ProfileManager;
import com.climpy.utils.C;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TestCommand extends RedrockCommand {

    public TestCommand() {
        super("test", "Test komutudur");
    }

    @Override
    public RedrockResultCommand execute(CommandSender commandSender, Command command, String label, String[] args) {
        if(isConsole(commandSender)) {
            return RedrockResultCommand.CONSOLE_ONLY;
        }

        if (args.length < 1) {
            setUsage(label + " <oyuncuAdı>");
            return RedrockResultCommand.INVALID_USAGE;
        }

        ProfileManager profileManager = RedrockPlugin.getInstance().getProfileManager();
        Profile targetProfile = profileManager.getProfile(args[0]);
        if(targetProfile == null) {
            setPlayer(args[0]);
            return RedrockResultCommand.INVALID_PLAYER;
        }

        commandSender.sendMessage(C.GREEN + "Şu oyuncu bulundu: " + C.WHITE + targetProfile.getName());
        return RedrockResultCommand.SUCCESS;
    }
}
