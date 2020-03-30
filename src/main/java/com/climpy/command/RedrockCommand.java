package com.climpy.command;

import com.climpy.utils.C;
import com.climpy.utils.ClickableUtil;
import com.climpy.utils.MiscUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter @Setter
public abstract class RedrockCommand implements CommandExecutor, TabCompleter {
    private String name;

    private String description;

    private String[] aliases;

    private String usage;

    private String permission;

    private String player;

    private String subCommand;

    private String argument;

    public RedrockCommand(String name) {
        this(name, null, ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public RedrockCommand(String name, String description) {
        this(name, description, ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public RedrockCommand(String name, String description, String[] aliases) {
        this(name, description, aliases, null, null);
    }

    public RedrockCommand(String name, String description, String[] aliases, String usage, String permission) {
        this.name = name;
        this.description = description;
        this.aliases = Arrays.<String>copyOf(aliases, aliases.length);
        this.usage = usage;
        this.permission = permission;
    }

    public boolean onCommand(CommandSender commandsender, Command command, String label, String[] arguments) {
        if (getPermission() != null &&
                !commandsender.hasPermission(getPermission())) {
            commandsender.sendMessage(command.getPermissionMessage());
            return true;
        }

        try {
            RedrockResultCommand result = execute(commandsender, command, label, arguments);
            if (result == RedrockResultCommand.INVALID_USAGE) {
                sendActionsUsage(commandsender, getUsage());
            } else if (result == RedrockResultCommand.INVALID_PERMISSION) {
                commandsender.sendMessage(command.getPermissionMessage());
            } else if (result == RedrockResultCommand.CONSOLE_ONLY) {
                commandsender.sendMessage(C.RED + "Maalesef, bu komut sadece konsol aracılığıyla yürütülebilir.");
            } else if (result == RedrockResultCommand.CRITICAL_ERROR) {
                commandsender.sendMessage("&cOops... Bu komut gerçekleşirken bir hata oluştu, lütfen personele bildirin.");
            } else if (result == RedrockResultCommand.INVALID_PLAYER) {
                commandsender.sendMessage(C.WHITE + "'" + getPlayer() + "'" + C.RED + " adlı oyuncu bulunamadı.");
            } else if (result == RedrockResultCommand.INVALID_ARGUMENT) {
                commandsender.sendMessage(C.WHITE + "'" + getSubCommand() + "'" + C.RED + " geçersiz bir alt-komut.");
            } else if (result == RedrockResultCommand.INVALID_ARGUMENT) {
                commandsender.sendMessage(C.WHITE + "'" + getArgument() + "'" + C.RED + " geçersiz, argument bulunamadı..");
            }
        } catch (Exception e) {
            e.printStackTrace();
            commandsender.sendMessage("&cOops... Bu komut gerçekleşirken bir hata oluştu, lütfen personele bildirin.");
        }

        return true;
    }

    public abstract RedrockResultCommand execute(CommandSender commandSender, Command command, String label, String[] args);

    public List<String> onTabComplete(CommandSender commandsender, Command command, String label, String[] arguments) {
        List<String> results = new ArrayList<>();
        if (results.isEmpty())
            return null;
        return MiscUtils.getCompletions(arguments, results);
    }

    public void sendActionsUsage(CommandSender commandsender, String value) {
        commandsender.sendMessage(C.RED + "Kullanım: /" + value + C.GRAY + " - " + C.WHITE + this.description);
    }

    public boolean isConsole(CommandSender commandsender) {
        return !(commandsender instanceof org.bukkit.entity.Player);
    }

    public boolean hasPermission(CommandSender commandsender, String permission) {
        return commandsender.hasPermission(permission);
    }
}
