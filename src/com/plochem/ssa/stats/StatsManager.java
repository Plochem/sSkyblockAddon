package com.plochem.ssa.stats;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.plochem.ssa.SSkyblockAddon;
import com.wasteofplastic.askyblock.ASkyBlockAPI;


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
		SSkyblockAddon sfa = SSkyblockAddon.getPlugin(SSkyblockAddon.class);
		if(p==null)
			return;
        new BukkitRunnable() {
            @Override
            public void run() {
            	String islandName = ASkyBlockAPI.getInstance().getIslandName(p.getUniqueId());
            	long islandLevel = ASkyBlockAPI.getInstance().getLongIslandLevel((p.getUniqueId()));
            	if(ASkyBlockAPI.getInstance().getIslandOwnedBy(p.getUniqueId()) == null) // doesnt own/part of island
            		islandName = "None";
            	if(ASkyBlockAPI.getInstance().inTeam(p.getUniqueId())) {// if in team, get the team's leader uuid to get island name
            		UUID owner = ASkyBlockAPI.getInstance().getTeamLeader(p.getUniqueId());
            		islandName = ASkyBlockAPI.getInstance().getIslandName(owner);
            		islandLevel = ASkyBlockAPI.getInstance().getLongIslandLevel(owner);
            	}

        		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
                Objective obj = sb.registerNewObjective("stats", "dummy");
                obj.setDisplaySlot(DisplaySlot.SIDEBAR);
                obj.setDisplayName("§e§lStraxidus Skyblock");
                obj.getScore(StringUtils.center("§7Statistics", 33)).setScore(6);
                obj.getScore("Kills: §a" + getKills(p)).setScore(5);
                obj.getScore("Deaths: §a" + getDeaths(p)).setScore(4);
                obj.getScore("Balance: §a$" + String.format("%,.2f", sfa.getSEconomy().getEconomyImplementer().getBalance(p))).setScore(3);
                obj.getScore("").setScore(2);
                obj.getScore("Island name: §a" + islandName).setScore(1);
                obj.getScore("Island level: §a" + islandLevel).setScore(0);
                p.setScoreboard(sb);
            }
        }.runTaskLater(sfa, 20);

	}
	
	
}
