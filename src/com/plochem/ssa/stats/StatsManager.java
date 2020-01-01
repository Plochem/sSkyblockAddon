package com.plochem.ssa.stats;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Map.Entry;
import java.util.TreeMap;

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
	private static TreeMap<Long, String> suffixes = new TreeMap<>();
	static {
		suffixes.put(1000L, "k");
		suffixes.put(1000000L, "M");
		suffixes.put(1000000000L, "B"); // i know it's supposed to be G
		suffixes.put(1000000000000L, "T");
		suffixes.put(1000000000000000L, "Q");// i know it's supposed to be P
	}
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
					numMembers = curr.getIslandMembers(true).size();
				}

				Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
				
				Objective obj = sb.registerNewObjective("stats", "dummy");
				obj.setDisplaySlot(DisplaySlot.SIDEBAR);
				obj.setDisplayName("§b§lSKYBLOCK");
				obj.getScore("§f---------------------").setScore(11);
				obj.getScore("§b§lYou").setScore(10);
				obj.getScore("§3❖ §fKills: §b" + getKills(p)).setScore(9);
				obj.getScore("§3❖ §fDeaths: §b" + getDeaths(p)).setScore(8);
				obj.getScore("§3❖ §fBalance: §b$" + formatValue(sfa.getSEconomy().getEconomyImplementer().getBalance(p))).setScore(7);
				obj.getScore("§3❖ §fXP: §b" + formatValue(getPlayerExp(p))).setScore(6);
				obj.getScore("").setScore(5);
				obj.getScore("§b§lIsland").setScore(4);
				obj.getScore("§3❖ §fIsland name: §b" + islandName).setScore(3);
				obj.getScore("§3❖ §fIsland level: §b" + islandLevel).setScore(2);
				obj.getScore("§3❖ §fMember count: §b" + numMembers).setScore(1);
				obj.getScore("---------------------").setScore(0);
				p.setScoreboard(sb);
			}
		}.runTaskLater(sfa, 20);

	}
	
    // # EXP to level up at current level
    public static int getExpToLevelUp(int level){
        if(level <= 15){
            return 2*level+7;
        } else if(level <= 30){
            return 5*level-38;
        } else {
            return 9*level-158;
        }
    }
  
    // # of exp up to current level
    public static int getExpAtLevel(int level){
        if(level <= 16){
            return (int) (Math.pow(level,2) + 6*level);
        } else if(level <= 31){
            return (int) (2.5*Math.pow(level,2) - 40.5*level + 360.0);
        } else {
            return (int) (4.5*Math.pow(level,2) - 162.5*level + 2220.0);
        }
    }
	
    public static int getPlayerExp(Player player){
        int exp = 0;
        int level = player.getLevel();
        exp += getExpAtLevel(level);
        // needed xp to level up in current level * percent filled by player
        // Mojang is wack af
        exp += Math.round(getExpToLevelUp(level) * player.getExp()); 
        return exp;
    }

	public static String formatValue(double num) {
		if (num < 1000) 
			return String.format("%,.2f", num);
		long value = (long)num;
		Entry<Long, String> e = suffixes.floorEntry(value);
		Long divideBy = e.getKey();
		String suffix = e.getValue();

		long truncated = value / (divideBy / 10);
		boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
		return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
	}


}
