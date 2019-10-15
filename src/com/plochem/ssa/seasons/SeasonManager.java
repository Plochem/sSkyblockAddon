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
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.handlers.GridHandler;
import com.bgsoftware.superiorskyblock.island.IslandRegistry;
import com.plochem.ssa.SSkyblockAddon;

public class SeasonManager {
	private static File seasonFile =  new File("plugins/SFA/seasons/season.yml");
	private static YamlConfiguration seasonData = YamlConfiguration.loadConfiguration(seasonFile);

	public static void createSeasonFile() {
		if(!(seasonFile.exists())) {
			Bukkit.getServer().getLogger().info("[SFA] Creating season rewards file!");
			save(seasonFile, seasonData);
		}  else {
			Bukkit.getServer().getLogger().info("[SFA] Season rewards file already exists! Skipping creation...");
		}
	}

	public static void startTimer() {
		LocalDateTime tomorrowMidnight = LocalDate.now().plusDays(1).atStartOfDay();
		long midnight = LocalDateTime.now().until(tomorrowMidnight, ChronoUnit.SECONDS); // time till midnight
		
		LocalDate current = LocalDate.now();
		int daysOfMonth = current.lengthOfMonth();
		int currentDay = current.getDayOfMonth() + 1; // day of tomorrow
		long secondsTillEndMonth = TimeUnit.DAYS.toSeconds(daysOfMonth - currentDay + 1);
		ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
		schedule.schedule(new Runnable() {
			@Override
			public void run() {
				prepNewSeason();
				schedule.shutdown();
				startTimer();
			}
		}, midnight + secondsTillEndMonth, TimeUnit.SECONDS);
	}
	
	public static void prepNewSeason() {
		File f = new File("plugins/SFA/seasons/season.yml");
		YamlConfiguration fData = YamlConfiguration.loadConfiguration(f);
		int justEndedSeason = fData.getInt("season");
		fData.set("season", justEndedSeason+1);
		save(f, fData); // update season number
		try {
			Field islands = GridHandler.class.getDeclaredField("islands");
			islands.setAccessible(true);
			IslandRegistry is = (IslandRegistry)islands.get(SuperiorSkyblockPlugin.getPlugin().getGrid());
			is.sort();
			for(File data : new File("plugins/SFA/seasons/playerdata").listFiles()) {
				YamlConfiguration c = YamlConfiguration.loadConfiguration(data);
				for(int i = 0; i < is.size(); i++) {
					if(is.get(i).getAllMembers().contains(UUID.fromString(FilenameUtils.removeExtension(data.getName())))) {
						c.set(String.valueOf(fData.get("season")) + ".rank", i+1);
						save(data, c);
						break;
					}
				}
			}
			
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException e1) {
			e1.printStackTrace();
		}
		
		SSkyblockAddon.getPlugin(SSkyblockAddon.class).getSEconomy().clearBalances();
		
	}

	public static void refreshRewards() {
		seasonData = YamlConfiguration.loadConfiguration(seasonFile);
		save(seasonFile, seasonData);
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
		Inventory menu = Bukkit.createInventory(null, 45, "Season Rewards");
		int[] firstSlots = {12,13,14,15,16};
		int[] secondSlots = {30,31,32,33,34};
		int seasonNum = getCurrentSeason();
		int prevSeason = seasonNum - 1;
		int idx = 0;
		ItemStack seasonItem = new ItemStack(Material.BEACON);
		ItemMeta seasonItemMeta = seasonItem.getItemMeta();
		ItemStack menuFiller = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)15);
		if(prevSeason != 0) {
			seasonItemMeta.setDisplayName("§eSeason " + prevSeason + " Rewards");
			seasonItem.setAmount(prevSeason);
			seasonItem.setItemMeta(seasonItemMeta);
			menu.setItem(10, seasonItem);
			for(String reward : seasonData.getConfigurationSection(String.valueOf(prevSeason)).getKeys(false)) {
				ItemStack item = seasonData.getConfigurationSection(String.valueOf(prevSeason)).getItemStack(reward + ".itemRep");
				item.setAmount(prevSeason);
				menu.setItem(firstSlots[idx], item);
				idx++;
			}
		}

		idx = 0;
		seasonItemMeta.setDisplayName("§eSeason " + seasonNum + " Rewards");
		seasonItem.setAmount(seasonNum);
		seasonItem.setItemMeta(seasonItemMeta);
		menu.setItem(28, seasonItem);
		for(String reward : seasonData.getConfigurationSection(String.valueOf(seasonNum)).getKeys(false)) {
			ItemStack item = seasonData.getConfigurationSection(String.valueOf(seasonNum)).getItemStack(reward + ".itemRep");
			item.setAmount(seasonNum);
			menu.setItem(secondSlots[idx], item);
			idx++;
		}

		for(int i = 0; i < menu.getSize(); i++) {
			if(menu.getItem(i) == null) {
				menu.setItem(i, menuFiller);
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
