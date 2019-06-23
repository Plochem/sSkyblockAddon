package com.plochem.sfa.rewards;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RewardManager {
	public static void openRewardMenu(Player p) {
		Inventory menu = Bukkit.createInventory(null, 36, "Rewards");
		ItemStack i = new ItemStack(Material.STORAGE_MINECART);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName("§eDaily Rewards");
		im.setLore(Arrays.asList(""));
		i.setItemMeta(im);
		menu.setItem(10, i);
		
		im.setDisplayName("§eMonthly Rewards");
		im.setLore(Arrays.asList(""));
		i.setItemMeta(im);
		menu.setItem(11, i);
		
		im.setDisplayName("§eElite Monthly Rewards");
		im.setLore(Arrays.asList(""));
		i.setItemMeta(im);
		menu.setItem(12, i);
		
		im.setDisplayName("§eMaster Monthly Rewards");
		im.setLore(Arrays.asList(""));
		i.setItemMeta(im);
		menu.setItem(13, i);
		
		im.setDisplayName("§eLegend Monthly Rewards");
		im.setLore(Arrays.asList(""));
		i.setItemMeta(im);
		menu.setItem(14, i);
		
		im.setDisplayName("§eMystic Monthly Rewards");
		im.setLore(Arrays.asList(""));
		i.setItemMeta(im);
		menu.setItem(15, i);
		p.openInventory(menu);
	}
	
	public static void createCollectedFile() {
		File f = new File("plugins/SFA/claimedplayers.yml");
		YamlConfiguration fData = YamlConfiguration.loadConfiguration(f);
		if(!(f.exists())) {
			Bukkit.getServer().getLogger().info("[SFA] Creating a claimed reward file!");
			fData.set(RewardType.DAILY.toString(), new ArrayList<String>());
			fData.set(RewardType.MONTHLY.toString(), new ArrayList<String>());
			fData.set(RewardType.ELITEMONTHLY.toString(), new ArrayList<String>());
			fData.set(RewardType.MASTERMONTHLY.toString(), new ArrayList<String>());
			fData.set(RewardType.LEGENDMONTHLY.toString(), new ArrayList<String>());
			fData.set(RewardType.MYSTICMONTHLY.toString(), new ArrayList<String>());
			try {
				fData.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}  else {
			Bukkit.getServer().getLogger().info("[SFA] Claimed reward file already exists! Skipping creation...");
		}
	}
	
	public static void resetListTimer() {
		Timer timer = new Timer();
		LocalDateTime tomorrowMidnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).plusDays(1);
		Date date = Date.from(tomorrowMidnight.atZone(ZoneId.systemDefault()).toInstant());
		
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() { // runs when is tomorrowMidnight
				File f = new File("plugins/SFA/claimedplayers.yml");
				YamlConfiguration fData = YamlConfiguration.loadConfiguration(f);
				for(RewardType type : RewardType.values()) {
					if(type.toString().contains("MONTHLY")) {
						if(tomorrowMidnight.getDayOfMonth() == 1) { // check if the midnight is the first of month
							fData.set(type.toString(), new ArrayList<>()); // then clear monthly
						}
					} else { // clear daily
						fData.set(type.toString(), new ArrayList<>());
					}
				}
				try {
					fData.save(f);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, date, TimeUnit.DAYS.toMillis(1));
	}
	
	@SuppressWarnings("unchecked")
	public static void addToClaim(Player clicker, RewardType type) {
		File f = new File("plugins/SFA/claimedplayers.yml");
		YamlConfiguration fData = YamlConfiguration.loadConfiguration(f);
		List<String> claimed = (List<String>)(fData.getList(type.toString()));	
		claimed.add(clicker.getUniqueId().toString());
		fData.set(type.toString(), claimed);
		try {
			fData.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static boolean notClaim(Player clicker, RewardType type) {
		File f = new File("plugins/SFA/claimedplayers.yml");
		YamlConfiguration fData = YamlConfiguration.loadConfiguration(f);
		List<String> claimed = (List<String>)(fData.getList(type.toString()));
		if(claimed.contains(clicker.getUniqueId().toString())) { // in list = claimed
			return false;
		}
		return true;
	}
	
}
