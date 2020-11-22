package com.climpy.redrock.commands.handler;

import com.climpy.redrock.RedrockPlugin;
import com.climpy.redrock.commands.RedrockRegisterCommand;
import com.climpy.redrock.commands.network.*;
import com.climpy.redrock.commands.network.players.ReportCommand;
import com.climpy.redrock.commands.network.players.RequestCommand;
import com.climpy.redrock.report.menu.commands.ReportsCommand;
import com.climpy.redrock.server.commands.ServerChangeCommand;
import com.climpy.redrock.server.commands.ServerUpdateCommand;

public class RedrockCommandHandler {

    public static void init() {
        RedrockRegisterCommand redrockRegisterCommand = new RedrockRegisterCommand();


        redrockRegisterCommand.registerBukkitCommand(new AlertCommand(), RedrockPlugin.getInstance());
        redrockRegisterCommand.registerBukkitCommand(new ServerCommand(), RedrockPlugin.getInstance());
        redrockRegisterCommand.registerBukkitCommand(new ReportCommand(), RedrockPlugin.getInstance());
        redrockRegisterCommand.registerBukkitCommand(new FindCommand(), RedrockPlugin.getInstance());
        redrockRegisterCommand.registerBukkitCommand(new RequestCommand(), RedrockPlugin.getInstance());
        redrockRegisterCommand.registerBukkitCommand(new LobbyCommand(), RedrockPlugin.getInstance());
        redrockRegisterCommand.registerBukkitCommand(new StatusCommand(), RedrockPlugin.getInstance());
        redrockRegisterCommand.registerBukkitCommand(new NetworkPlayersCommand(), RedrockPlugin.getInstance());
        redrockRegisterCommand.registerBukkitCommand(new ServerUpdateCommand(), RedrockPlugin.getInstance());
        redrockRegisterCommand.registerBukkitCommand(new ServerChangeCommand(), RedrockPlugin.getInstance());
        redrockRegisterCommand.registerBukkitCommand(new ReportsCommand(), RedrockPlugin.getInstance());
    }
}
