package com.plochem.ssa.stats;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.plochem.ssa.SSkyblockAddon;



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
            @SuppressWarnings("deprecation")
			@Override
            public void run() {
            	Island curr = SuperiorSkyblockAPI.getPlayer(p).getIsland();
            	String islandName = "None";
            	int islandLevel = 0;
            	int numMembers = 0;
            	if(curr != null) {
                	islandName = curr.getName();
                	islandLevel = curr.getIslandLevel();
                	numMembers = curr.getAllMembers().size();
            	}

        		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
                Objective obj = sb.registerNewObjective("stats", "dummy");
                obj.setDisplaySlot(DisplaySlot.SIDEBAR);
                obj.setDisplayName("§e§lStraxidus Skyblock");
                obj.getScore(StringUtils.center("§7Statistics", 33)).setScore(7);
                obj.getScore("Kills: §a" + getKills(p)).setScore(6);
                obj.getScore("Deaths: §a" + getDeaths(p)).setScore(5);
                obj.getScore("Balance: §a$" + String.format("%,.2f", sfa.getSEconomy().getEconomyImplementer().getBalance(p))).setScore(4);
                obj.getScore("").setScore(3);
                obj.getScore("Island name: §a" + islandName).setScore(2);
                obj.getScore("Island level: §a" + islandLevel).setScore(1);
                obj.getScore("Number of members: §a" + numMembers).setScore(0);
                p.setScoreboard(sb);
            }
        }.runTaskLater(sfa, 20);

	}
	
	@SuppressWarnings("deprecation")
	public static void updateTab(Player joiner) {
		if(joiner != null) {
        	Island curr =  SuperiorSkyblockAPI.getPlayer(joiner).getIsland();
        	int islandLevel = 0;
        	if(curr != null)
        		islandLevel = curr.getIslandLevel();
			joiner.setPlayerListName(joiner.getPlayerListName() + " §7[§a" + islandLevel + "§7]");
		}	
	}
	
	
}
