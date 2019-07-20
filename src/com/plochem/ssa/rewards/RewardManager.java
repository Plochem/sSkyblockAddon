package com.plochem.ssa.rewards;

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

import org.apache.commons.lang3.text.WordUtils;
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
		int[] pos = {10,11,12,13,14,15,16,19};
		for(int j = 0; j < RewardType.values().length; j++) {
			im.setDisplayName("§e" + WordUtils.capitalizeFully(RewardType.values()[j].toString().replaceAll("_", " ")) + " Rewards");
			List<String> lore = new ArrayList<>(Arrays.asList("§6$" + RewardType.values()[j].getMoney(),
												"§3"+RewardType.values()[j].getXp() + " XP", 
												"§7" + RewardType.values()[j].getCrateKey() + " key",
												"")); 
			if(notClaim(p, RewardType.values()[j])) {
				lore.add("§eClick to claim!");
				i.setType(Material.STORAGE_MINECART);
			} else {
				lore.add("§cAlready claimed!");
				i.setType(Material.MINECART);
			}
			im.setLore(lore);
			i.setItemMeta(im);
			menu.setItem(pos[j], i);
		}
		p.openInventory(menu);
	}
	
	public static void createCollectedFile() {
		File f = new File("plugins/SFA/claimedplayers.yml");
		YamlConfiguration fData = YamlConfiguration.loadConfiguration(f);
		if(!(f.exists())) {
			Bukkit.getServer().getLogger().info("[SFA] Creating a claimed reward file!");
			fData.set(RewardType.DAILY.toString(), new ArrayList<String>());
			fData.set(RewardType.MONTHLY.toString(), new ArrayList<String>());
			fData.set(RewardType.ELITE_MONTHLY.toString(), new ArrayList<String>());
			fData.set(RewardType.MASTER_MONTHLY.toString(), new ArrayList<String>());
			fData.set(RewardType.LEGEND_MONTHLY.toString(), new ArrayList<String>());
			fData.set(RewardType.MYSTIC_MONTHLY.toString(), new ArrayList<String>());
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
