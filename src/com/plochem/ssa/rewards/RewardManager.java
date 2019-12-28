package com.plochem.ssa.rewards;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;

public class RewardManager {
	private static List<Reward> rewards = new ArrayList<>();
	private static File rewardsFile = new File("plugins/SFA/playerRewards/rewards.yml");
	private static File claimedPlayersFile = new File("plugins/SFA/playerRewards/claimedplayers.yml");
	private static YamlConfiguration rewardsData = YamlConfiguration.loadConfiguration(rewardsFile);
	private static YamlConfiguration claimedPlayers = YamlConfiguration.loadConfiguration(claimedPlayersFile);
	
	public static void openRewardMenu(Player p) {
		if(rewards == null || rewards.size() == 0) {
			p.sendMessage("§cNo rewards are available!");
			return;
		}
		Inventory menu = Bukkit.createInventory(null, 36, "Rewards");
		ItemStack i = new ItemStack(Material.STORAGE_MINECART);
		ItemMeta im = i.getItemMeta(); 
		for(Reward r : rewards) {
			im.setDisplayName(r.getName());
			List<String> currLore = new ArrayList<>(r.getLore());
			currLore.add(""); // empty line
			if(notClaim(p, r)) {
				currLore.add("§eClick to claim!");
				i.setType(Material.STORAGE_MINECART);
			} else {
				currLore.add("§cAlready claimed!");
				i.setType(Material.MINECART);
			}
			im.setLore(currLore);
			i.setItemMeta(im);
			menu.setItem(r.getPosition(), i);
		}
		p.openInventory(menu);
	}

	@SuppressWarnings("unchecked")
	public static void createFiles() {
		if(!(claimedPlayersFile.exists())) {
			Bukkit.getServer().getLogger().info("[SFA] Creating a claimed reward file!");
			try {
				claimedPlayers.save(claimedPlayersFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}  else {
			Bukkit.getServer().getLogger().info("[SFA] Claimed reward file already exists! Skipping creation...");
		}
		
		if(!(rewardsFile.exists())) {
			Bukkit.getServer().getLogger().info("[SFA] Creating a rewards file!");
			try {
				rewardsData.save(rewardsFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}  else {
			Bukkit.getServer().getLogger().info("[SFA] Rewards file already exists! Skipping creation...");
		}
		
		rewards = (List<Reward>) rewardsData.getList("rewards");
		if(rewards == null) rewards = new ArrayList<>();
	}
	
	public static void registerPerms() {
		for(Reward r : rewards) {
			Bukkit.getPluginManager().addPermission(new Permission(r.getPermission()));
		}
	}

	public static void resetListTimer() {
		LocalDateTime tomorrowMidnight = LocalDate.now().plusDays(1).atStartOfDay();
		ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
		long midnight = LocalDateTime.now().until(tomorrowMidnight, ChronoUnit.MINUTES);
		schedule.scheduleAtFixedRate(new Runnable() { // run every midnight
			@Override
			public void run() {
				for(Reward r : rewards) {
					if(r.getType() == RewardType.MONTHLY) {
						if(tomorrowMidnight.getDayOfMonth() == 1) { // check if the midnight is the first of month
							claimedPlayers.set(r.getName().toString(), new ArrayList<>()); // then clear monthly
						}
					} else if(r.getType() == RewardType.YEARLY){
						if(tomorrowMidnight.getDayOfMonth() == 1 && tomorrowMidnight.getMonth().getValue() == 1) {
							claimedPlayers.set(r.getName().toString(), new ArrayList<>());
						}
					}
					else { // clear daily
						claimedPlayers.set(r.getName().toString(), new ArrayList<>());
					}
				}
				try {
					claimedPlayers.save(claimedPlayersFile);
				} catch (IOException e) { 
					e.printStackTrace();
				}

			}
		}, midnight, TimeUnit.DAYS.toMinutes(1) - 1, TimeUnit.MINUTES);

	}

	@SuppressWarnings("unchecked")
	public static void addToClaim(Player clicker, Reward r) {
		List<String> claimed = (List<String>)(claimedPlayers.getList(r.getName()));	
		if(claimed == null) claimed = new ArrayList<String>();
		claimed.add(clicker.getUniqueId().toString());
		claimedPlayers.set(r.getName(), claimed);
		try {
			claimedPlayers.save(claimedPlayersFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static boolean notClaim(Player clicker, Reward r) {
		List<String> claimed = (List<String>)(claimedPlayers.getList(r.getName()));
		if(claimed == null) return true;
		if(claimed.contains(clicker.getUniqueId().toString())) { // in list = claimed
			return false;
		}
		return true;
	}
	
	public static List<Reward> getRewards(){
		return rewards;
	}
	
	@SuppressWarnings("unchecked")
	public static void reload() {
		rewardsData = YamlConfiguration.loadConfiguration(rewardsFile);
		rewards = (List<Reward>) rewardsData.getList("rewards");
	}

}
