package com.climpy.command;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import com.climpy.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

public class RedrockRegisterCommand {
    public void registerBukkitCommand(RedrockCommand redrockCommand, Plugin plugin) {
        registerBukkitCommand(redrockCommand, redrockCommand.getName(), redrockCommand.getAliases(), plugin);
    }

    public void registerBukkitCommand(RedrockCommand redrockCommand, String name, String[] aliases, Plugin plugin) {
        PluginCommand command = getCommand(name, plugin);
        if (aliases != null)
            command.setAliases(Arrays.asList(aliases));
        if (redrockCommand.getDescription() != null)
            command.setDescription(redrockCommand.getDescription());
        command.setPermissionMessage(C.RED + "Üzgünüm, yetkiniz yok.");
        command.setExecutor(redrockCommand);
        command.setTabCompleter(redrockCommand);
        getCommandMap().register(plugin.getDescription().getName(), (Command)command);
    }

    public void unregisterBukkitCommand(RedrockCommand redrockCommand, Plugin plugin) {
        unregisterBukkitCommand(redrockCommand, redrockCommand.getName(), plugin);
    }

    public void unregisterBukkitCommand(RedrockCommand redrockCommand, String name, Plugin plugin) {
        PluginCommand command = getCommand(name, plugin);
        command.unregister(getCommandMap());
    }

    private PluginCommand getCommand(String name, Plugin plugin) {
        PluginCommand command = null;
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(new Class[] { String.class, Plugin.class });
            constructor.setAccessible(true);
            command = constructor.newInstance(new Object[] { name, plugin });
        } catch (SecurityException|NoSuchMethodException|java.lang.reflect.InvocationTargetException|InstantiationException|IllegalAccessException|IllegalArgumentException e) {
            e.printStackTrace();
        }
        return command;
    }

    private CommandMap getCommandMap() {
        CommandMap commandMap = null;
        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                Field f = SimplePluginManager.class.getDeclaredField("commandMap");
                f.setAccessible(true);
                commandMap = (CommandMap)f.get(Bukkit.getPluginManager());
            }
        } catch (NoSuchFieldException|IllegalAccessException|IllegalArgumentException|SecurityException e) {
            e.printStackTrace();
        }
        return commandMap;
    }
}
