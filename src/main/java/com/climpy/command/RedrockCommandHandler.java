package com.climpy.command;

import com.climpy.RedrockPlugin;
import com.climpy.command.commands.bukkit.*;
import com.climpy.command.commands.bukkit.teleport.TeleportAllCommand;
import com.climpy.command.commands.bukkit.teleport.TeleportCommand;
import com.climpy.command.commands.bukkit.teleport.TeleportHereCommand;
import com.climpy.command.commands.bukkit.teleport.TeleportPositionCommand;
import com.climpy.command.commands.chat.*;
import com.climpy.command.commands.core.*;
import com.climpy.command.commands.inventory.ClearCommand;
import com.climpy.command.commands.inventory.HatCommand;
import com.climpy.command.network.*;
import com.climpy.command.network.players.ReportCommand;
import com.climpy.command.network.players.RequestCommand;
import com.climpy.profile.commands.ProfileCommand;
import com.climpy.profile.commands.StaffChatCommand;
import com.climpy.profile.moderator.commands.FollowCommand;
import com.climpy.profile.moderator.commands.StaffModeCommand;
import com.climpy.profile.moderator.commands.UnFollowCommand;
import com.climpy.profile.moderator.commands.VanishCommand;
import com.climpy.profile.moderator.menu.InspectorCommand;
import com.climpy.profile.rank.commands.ListRankCommand;
import com.climpy.profile.rank.commands.SetRankCommand;
import com.climpy.profile.symbol.commands.SetSymbolCommand;
import com.climpy.profile.symbol.commands.SymbolCommand;
import com.climpy.punishments.args.*;
import com.climpy.punishments.history.HistoryCommand;
import com.climpy.report.menu.commands.ReportsCommand;
import com.climpy.server.commands.ServerChangeCommand;
import com.climpy.server.commands.ServerUpdateCommand;

public class RedrockCommandHandler {
    
    private RedrockPlugin redrockPlugin;
    
    private RedrockCommandHandler(RedrockPlugin redrockPlugin) {
        this.redrockPlugin = redrockPlugin;
    }
    
    public static void init() {
        RedrockPlugin.getInstance().getCommand("broadcast").setExecutor(new BroadcastCommand());
        RedrockPlugin.getInstance().getCommand("day").setExecutor(new DayCommand());
        RedrockPlugin.getInstance().getCommand("list").setExecutor(new ListCommand());
        RedrockPlugin.getInstance().getCommand("info").setExecutor(new InfoCommand());
        RedrockPlugin.getInstance().getCommand("night").setExecutor(new NightCommand());
        RedrockPlugin.getInstance().getCommand("createworld").setExecutor(new WorldCreateCommand());
        RedrockPlugin.getInstance().getCommand("world").setExecutor(new WorldTeleportCommand());
        RedrockPlugin.getInstance().getCommand("gamemode").setExecutor(new GameModeCommand());
        RedrockPlugin.getInstance().getCommand("chatclear").setExecutor(new ChatClearCommand());

        RedrockPlugin.getInstance().getCommand("clear").setExecutor(new ClearCommand());
        RedrockPlugin.getInstance().getCommand("setslots").setExecutor(new SetSlotsCommand());
        RedrockPlugin.getInstance().getCommand("hat").setExecutor(new HatCommand());
        RedrockPlugin.getInstance().getCommand("more").setExecutor(new MoreCommand());
        RedrockPlugin.getInstance().getCommand("fly").setExecutor(new FlyCommand());
        RedrockPlugin.getInstance().getCommand("ping").setExecutor(new PingCommand());
        RedrockPlugin.getInstance().getCommand("teleport").setExecutor(new TeleportCommand());
        RedrockPlugin.getInstance().getCommand("teleportall").setExecutor(new TeleportAllCommand());
        RedrockPlugin.getInstance().getCommand("tphere").setExecutor(new TeleportHereCommand());
        RedrockPlugin.getInstance().getCommand("tppos").setExecutor(new TeleportPositionCommand());
        RedrockPlugin.getInstance().getCommand("coins").setExecutor(new CoinsCommand());

        RedrockPlugin.getInstance().getCommand("alert").setExecutor(new AlertCommand());
        RedrockPlugin.getInstance().getCommand("server").setExecutor(new ServerCommand());
        RedrockPlugin.getInstance().getCommand("report").setExecutor(new ReportCommand());
        RedrockPlugin.getInstance().getCommand("request").setExecutor(new RequestCommand());
        RedrockPlugin.getInstance().getCommand("lobby").setExecutor(new LobbyCommand());
        RedrockPlugin.getInstance().getCommand("status").setExecutor(new StatusCommand());
        RedrockPlugin.getInstance().getCommand("glist").setExecutor(new NetworkPlayersCommand());
        RedrockPlugin.getInstance().getCommand("serverchange").setExecutor(new ServerChangeCommand());
        RedrockPlugin.getInstance().getCommand("serverupdate").setExecutor(new ServerUpdateCommand());
        RedrockPlugin.getInstance().getCommand("speed").setExecutor(new SpeedCommand());
        RedrockPlugin.getInstance().getCommand("sudo").setExecutor(new SudoCommand());
        RedrockPlugin.getInstance().getCommand("find").setExecutor(new FindCommand());
        RedrockPlugin.getInstance().getCommand("whitelist").setExecutor(new WhitelistCommand());

        RedrockPlugin.getInstance().getCommand("staffchat").setExecutor(new StaffChatCommand());
        RedrockPlugin.getInstance().getCommand("staffmode").setExecutor(new StaffModeCommand());
        RedrockPlugin.getInstance().getCommand("vanish").setExecutor(new VanishCommand());

        RedrockPlugin.getInstance().getCommand("rank").setExecutor(new SetRankCommand());
        RedrockPlugin.getInstance().getCommand("listrank").setExecutor(new ListRankCommand());

        RedrockPlugin.getInstance().getCommand("profile").setExecutor(new ProfileCommand());

        RedrockPlugin.getInstance().getCommand("symbol").setExecutor(new SymbolCommand());
        RedrockPlugin.getInstance().getCommand("setsymbol").setExecutor(new SetSymbolCommand());
        RedrockPlugin.getInstance().getCommand("whois").setExecutor(new WhoisCommand());

        RedrockPlugin.getInstance().getCommand("ban").setExecutor(new BanCommand());
        RedrockPlugin.getInstance().getCommand("blacklist").setExecutor(new BlacklistCommand());
        RedrockPlugin.getInstance().getCommand("kick").setExecutor(new KickCommand());
        RedrockPlugin.getInstance().getCommand("mute").setExecutor(new MuteCommand());
        RedrockPlugin.getInstance().getCommand("unban").setExecutor(new UnBanCommand());
        RedrockPlugin.getInstance().getCommand("unblacklist").setExecutor(new UnBlacklistCommand());
        RedrockPlugin.getInstance().getCommand("unmute").setExecutor(new UnMuteCommand());
        RedrockPlugin.getInstance().getCommand("warn").setExecutor(new WarnCommand());
        RedrockPlugin.getInstance().getCommand("ipmute").setExecutor(new IPMuteCommand());
        RedrockPlugin.getInstance().getCommand("ipban").setExecutor(new IPBanCommand());
        RedrockPlugin.getInstance().getCommand("alts").setExecutor(new AltsCommand());

        RedrockPlugin.getInstance().getCommand("history").setExecutor(new HistoryCommand());
        RedrockPlugin.getInstance().getCommand("reports").setExecutor(new ReportsCommand());

        RedrockPlugin.getInstance().getCommand("msg").setExecutor(new MessageCommand());
        RedrockPlugin.getInstance().getCommand("stream").setExecutor(new StreamingCommand());
        RedrockPlugin.getInstance().getCommand("heal").setExecutor(new HealCommand());

        RedrockPlugin.getInstance().getCommand("follow").setExecutor(new FollowCommand());
        RedrockPlugin.getInstance().getCommand("unfollow").setExecutor(new UnFollowCommand());

        RedrockPlugin.getInstance().getCommand("messagesounds").setExecutor(new PrivateMessagesSoundCommand());
        RedrockPlugin.getInstance().getCommand("messagesvisible").setExecutor(new PrivateMessagesVisibleCommand());
        RedrockPlugin.getInstance().getCommand("socialspy").setExecutor(new SocialSpyCommand());
        RedrockPlugin.getInstance().getCommand("inspector").setExecutor(new InspectorCommand());
    }
}
