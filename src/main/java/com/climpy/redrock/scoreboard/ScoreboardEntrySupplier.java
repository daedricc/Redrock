package com.climpy.redrock.scoreboard;

import org.bukkit.entity.Player;

import java.util.List;

public interface ScoreboardEntrySupplier {

    String getScoreboardTitle();

    List<String> getMaximizedLines(Player player);

    List<String> getMinimizedLines(Player player);


}
