package com.plochem.ssa.seasons;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SeasonManager {
	private static File seasonFile =  new File("plugins/SFA/seasons/season.yml");
	private static YamlConfiguration seasonData = YamlConfiguration.loadConfiguration(seasonFile);

	public static void createRepairFile() {
		if(!(seasonFile.exists())) {
			Bukkit.getServer().getLogger().info("[SFA] Creating season rewards file!");
			saveRepairFile();
		}  else {
			Bukkit.getServer().getLogger().info("[SFA] Season rewards file already exists! Skipping creation...");
		}
	}
	
	private static void saveRepairFile() {
		try {
			seasonData.save(seasonFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void refreshRewards() {
		seasonFile =  new File("plugins/SFA/seasons/season.yml");
		seasonData = YamlConfiguration.loadConfiguration(seasonFile);
		saveRepairFile();
	}
	
	/*
	 * Coal Tier
	 *      itemRep: ITemStack
	 *      commands:
	 *      - '/give money'
	 *       leastRank: 1
	 *       highestRank: 10
	 *       slot: 5
	 * 
	 */
	public static void openMenu(Player p) {
		Inventory menu = Bukkit.createInventory(null, 36, "Season Rewards");
		for(String seasonNum : seasonData.getKeys(false)) {
			if(NumberUtils.isNumber(seasonNum)) {
				for(String reward : seasonData.getConfigurationSection(seasonNum).getKeys(false)) {
					ItemStack item = seasonData.getConfigurationSection(seasonNum).getItemStack(reward + ".itemRep");
					int slot = seasonData.getConfigurationSection(seasonNum).getInt(reward + ".slot");
					menu.setItem(slot, item);
				}
			}
		}

		p.openInventory(menu);
	}
	
	public static int getLeastRank(int season, String reward){
		return seasonData.getConfigurationSection(String.valueOf(season)).getInt(reward + ".leastRank");
	}
	
	public static int getHighestRank(int season, String reward){
		return seasonData.getConfigurationSection(String.valueOf(season)).getInt(reward + ".highestRank");
	}
	
	public static List<String> getCommands(int season, String reward){
		return seasonData.getConfigurationSection(String.valueOf(season)).getStringList(reward + ".commands");
	}
	
	public static int getCurrentSeason() {
		return seasonData.getInt("season");
	}

	public static String getClaimMsg(int season, String reward) {
		return seasonData.getConfigurationSection(String.valueOf(season)).getString(reward + ".claimMessage");
	}
	
	public static int getRankInSeason(int season, Player p) {
		File playerFile = new File("plugins/SFA/seasons/playerdata/" + p.getUniqueId().toString() + ".yml");
		YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
		int rank = playerData.getInt(String.valueOf(season) + ".rank");
		return rank;
	}

	public static boolean alreadyClaimed(int seasonRewardIsIn, String reward, Player p) {
		File playerFile = new File("plugins/SFA/seasons/playerdata/" + p.getUniqueId().toString() + ".yml");
		YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
		return playerData.getBoolean(String.valueOf(seasonRewardIsIn) + "." + reward);

		/*
		 * each player:
		 * 
		 * 1    <== season #
		 * 	rank: 5 <== island rank
		 * 	rewards:
		 * 		coaltier: true/false
		 * 
		 *  
		 * 
		 */
	}

	public static void claimed(int seasonRewardIsIn, String reward, Player p) {
		File playerFile = new File("plugins/SFA/seasons/playerdata/" + p.getUniqueId().toString() + ".yml");
		YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
		playerData.set(String.valueOf(seasonRewardIsIn) + "." + reward, true);
		save(playerFile, playerData);
	}
	
	public static void save(File f, YamlConfiguration c) {
		try {
			c.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
