package com.plochem.ssa.seasons;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.handlers.GridHandler;
import com.bgsoftware.superiorskyblock.island.IslandRegistry;

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
	
	public static void startTimer() {
		LocalDateTime tomorrowMidnight = LocalDate.now().plusDays(1).atStartOfDay();
		ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
		long midnight = LocalDateTime.now().until(tomorrowMidnight, ChronoUnit.SECONDS);
		schedule.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				File f = new File("plugins/SFA/seasons/season.yml");
				YamlConfiguration fData = YamlConfiguration.loadConfiguration(f);
				if(LocalDateTime.now().getDayOfMonth() == LocalDate.now().lengthOfMonth()) { // 11:59:59 pm on the last day of the month
					int justEndedSeason = fData.getInt("season");
					fData.set("season", justEndedSeason+1);
					save(seasonFile, seasonData); // update season number
					
					try {
						Field islands = GridHandler.class.getDeclaredField("islands");
						islands.setAccessible(true);
						IslandRegistry is = (IslandRegistry)islands.get(SuperiorSkyblockPlugin.getPlugin().getGrid());
						is.sort();
						for(File data : new File("plugins/SFA/seasons/playerdata").listFiles()) {
							YamlConfiguration c = YamlConfiguration.loadConfiguration(data);
							for(int i = 0; i < is.size(); i++) {
								if(is.get(i).getAllMembers().contains(UUID.fromString(FilenameUtils.removeExtension(data.getName())))) {
									c.set(String.valueOf(justEndedSeason) + ".rank", i+1);
									save(data, c);
									break;
								}
							}
						}
						
					} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException e1) {
						e1.printStackTrace();
					}
					
				}

			}
		}, midnight, TimeUnit.DAYS.toSeconds(1), TimeUnit.SECONDS);
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
