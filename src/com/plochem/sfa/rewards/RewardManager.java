package com.plochem.sfa.rewards;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		
		im.setDisplayName("§eMonthly Rewards");
		im.setLore(Arrays.asList(""));
		i.setItemMeta(im);
		menu.setItem(12, i);
		
		im.setDisplayName("§eMonthly Rewards");
		im.setLore(Arrays.asList(""));
		i.setItemMeta(im);
		menu.setItem(13, i);
		
		im.setDisplayName("§eMonthly Rewards");
		im.setLore(Arrays.asList(""));
		i.setItemMeta(im);
		menu.setItem(14, i);
		
		im.setDisplayName("§eMonthly Rewards");
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
