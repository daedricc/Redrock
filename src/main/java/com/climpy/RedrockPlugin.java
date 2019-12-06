package com.climpy;

import com.climpy.commands.commands.*;
import com.climpy.config.FileConfig;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class RedrockPlugin extends JavaPlugin {
    private static RedrockPlugin instance;
    private FileConfig mainConfig;

    @Override
    public void onEnable() {
        instance = this;

        registerManagers();
        registerCommands();
    }

    private void registerManagers() {
        this.mainConfig = new FileConfig(this, "config.yml");
    }

    private void registerCommands() {
        getCommand("broadcast").setExecutor(new BroadcastCommand());
        getCommand("day").setExecutor(new DayCommand());
        getCommand("info").setExecutor(new InfoCommand());
        getCommand("night").setExecutor(new NightCommand());
        getCommand("createworld").setExecutor(new WorldCreateCommand());
        getCommand("world").setExecutor(new WorldTeleportCommand());
        getCommand("gamemode").setExecutor(new GameModeCommand());
        getCommand("chatclear").setExecutor(new ChatClearCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("setspawn").setExecutor(new SetSpawnCommand());
        getCommand("clear").setExecutor(new ClearCommand());
        getCommand("setslots").setExecutor(new SetSlotsCommand());
        getCommand("hat").setExecutor(new HatCommand());
        getCommand("more").setExecutor(new MoreCommand());
    }

    public static RedrockPlugin getInstance() { return instance; }
}
