package com.plochem.sfa.stats;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;


public class StatsManager {
	public static void addDeath(Player killed) {
		File playerFile = new File("plugins/SFA/playerStats/" + killed.getUniqueId().toString() + ".yml");
		YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
		playerData.set("deaths", playerData.getInt("deaths") + 1);
		try {
			playerData.save(playerFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void addKill(Player killer) {
		File playerFile = new File("plugins/SFA/playerStats/" + killer.getUniqueId().toString() + ".yml");
		YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
		playerData.set("kills", playerData.getInt("kills") + 1);
		try {
			playerData.save(playerFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int getKills(Player p) {
		File playerFile = new File("plugins/SFA/playerStats/" + p.getUniqueId().toString() + ".yml");
		YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
		return playerData.getInt("kills");
	}
	
	public static int getDeaths(Player p) {
		File playerFile = new File("plugins/SFA/playerStats/" + p.getUniqueId().toString() + ".yml");
		YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
		return playerData.getInt("deaths");
	}
	
	public static void showScoreboard(Player p) {
		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = sb.registerNewObjective("stats", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName("§e§lStraxidus Factions");
        obj.getScore("Kills: §a" + getKills(p)).setScore(1);
        obj.getScore("Deaths: §a" + getDeaths(p)).setScore(0);
		p.setScoreboard(sb);
	}
}
