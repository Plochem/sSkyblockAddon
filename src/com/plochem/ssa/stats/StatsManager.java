package com.plochem.ssa.stats;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;

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
			@Override
            public void run() {
            	Island curr = SuperiorSkyblockAPI.getPlayer(p).getIsland();
            	String islandName = "None";
            	BigInteger islandLevel = BigInteger.valueOf(0);
            	int numMembers = 0;
            	if(curr != null) {
                	islandName = curr.getName();
                	islandLevel = curr.getIslandLevelAsBigDecimal().toBigInteger();
                	numMembers = curr.getAllMembers().size();
            	}

        		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
                Objective obj = sb.registerNewObjective("stats", "dummy");
                obj.setDisplaySlot(DisplaySlot.SIDEBAR);
                obj.setDisplayName("§b§lStraxidus §3§lSkyblock");
                obj.getScore("§f————————————————————————————————").setScore(10);
                obj.getScore("§b§lYou").setScore(9);
                obj.getScore("§3❖ §fKills: §b" + getKills(p)).setScore(8);
                obj.getScore("§3❖ §fDeaths: §b" + getDeaths(p)).setScore(7);
                obj.getScore("§3❖ §fBalance: §b$" + formatValue(sfa.getSEconomy().getEconomyImplementer().getBalance(p))).setScore(6);
                obj.getScore("").setScore(5);
                obj.getScore("§b§lIsland").setScore(4);
                obj.getScore("§3❖ §fIsland name: §b" + islandName).setScore(3);
                obj.getScore("§3❖ §fIsland level: §b" + islandLevel).setScore(2);
                obj.getScore("§3❖ §fNumber of members: §b" + numMembers).setScore(1);
                obj.getScore("————————————————————————————————").setScore(0);
                p.setScoreboard(sb);
            }
        }.runTaskLater(sfa, 20);

	}
	
	public static String formatValue(double value) {
		int power; 
		String suffix = " KMGT";
		String formattedNumber = "";

		NumberFormat formatter = new DecimalFormat("#,###.##");
		power = (int)Math.log10(value);
		value = value/(Math.pow(10,(power/3)*3));
		formattedNumber=formatter.format(value);
		formattedNumber = formattedNumber + suffix.charAt(power/3);
		return formattedNumber.length()>4 ?  formattedNumber.replaceAll("\\.[0-9]+", "") : formattedNumber;  
	}

	
}
