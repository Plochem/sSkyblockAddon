package com.plochem.ssa.kits;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.scheduler.BukkitRunnable;

import com.plochem.ssa.SSkyblockAddon;

public class KitManager {
	private static File kitFile = new File("plugins/SFA/kits.yml");
	private static YamlConfiguration kitData = YamlConfiguration.loadConfiguration(kitFile);
	private static List<Kit> kits = new ArrayList<>();
	
	static {
        new BukkitRunnable() {
            @Override
            public void run() {
            	for(File f : new File("plugins/SFA/kitcooldowns").listFiles()) {
            		YamlConfiguration cooldown = YamlConfiguration.loadConfiguration(f);
            		for(Kit kit : kits) {
            			int curr = cooldown.getInt(kit.getName() + "-time");
            			if(curr != 0) {
            				 cooldown.set(kit.getName() + "-time", curr-60);
            			}
            		}
            		saveCoolDown(f,cooldown);
            	}
            }
        }.runTaskTimerAsynchronously(SSkyblockAddon.getPlugin(SSkyblockAddon.class), 0, 20*60); // runs every minute
	}
	@SuppressWarnings("unchecked")
	public static void createKitFile() {
		if(!(kitFile.exists())) {
			Bukkit.getServer().getLogger().info("[SFA] Creating kit storage file!");
			saveKitFile();
		}  else {
			Bukkit.getServer().getLogger().info("[SFA] Kit storage file already exists! Skipping creation...");
			kits = ((List<Kit>)kitData.getList("kits"));
			if(kits==null) kits = new ArrayList<>();
			for(Kit k : kits) {
				Bukkit.getPluginManager().addPermission(new Permission("sfa.kits." + k.getName().toLowerCase()));
			}
		}
	}
	
	public static void saveCoolDown(File f, YamlConfiguration c) {
		try {
			c.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int getCoolDown(UUID id, String kitName) {
		File f =  new File("plugins/SFA/kitcooldowns/" + id.toString() + ".yml");
		YamlConfiguration cooldown = YamlConfiguration.loadConfiguration(f);
		return cooldown.getInt(kitName + "-time");
	}
	
	public static void saveKitFile() {
		kitData.set("kits", kits);
		try {
			kitData.save(kitFile);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public static void setCoolDown(UUID id, String kitName, int timeLeft) {
		File f =  new File("plugins/SFA/kitcooldowns/" + id.toString() + ".yml");
		YamlConfiguration cooldown = YamlConfiguration.loadConfiguration(f);
		cooldown.set(kitName + "-time", timeLeft);
		saveCoolDown(f, cooldown);
	}
	
	public static void openMenu(Player p) {
		Inventory menu = Bukkit.createInventory(p, 9, "Kit Menu");
		for(Kit k : kits) {
			ItemStack i = k.getItemRepNotOwn();		
			if(p.hasPermission("sfa.kits." + k.getName().toLowerCase())) {
				i = k.getItemRepOwn();
			}
			ItemMeta im = i.getItemMeta();
			im.setDisplayName("§f" + k.getName());
			im.setLore(Arrays.asList("§eLeft click to claim the kit!", "", "§eRight click to preview the kit!"));
			i.setItemMeta(im);
			menu.addItem(i);
		}
		p.openInventory(menu);
	}
	
	public static List<Kit> getKits(){
		return kits;
	}
	
	public static void addKit(Kit kit) {
		kits.add(kit);
		saveKitFile();
	}
	
	public static void giveKit(Player p, Kit k) {
		int cooldown = getCoolDown(p.getUniqueId(), k.getName());
		if(cooldown == 0 || p.isOp()) {
			boolean drop = false;
			for(ItemStack i : k.getItems()) {
				if(p.getInventory().firstEmpty() == -1) {
					p.getWorld().dropItem(p.getLocation(), i);
					drop = true;
				} else {
					p.getInventory().addItem(i);
				}
			}
			p.sendMessage("§aYou received the §e" + k.getName() + " §akit!");
			if(drop) {
				p.sendMessage("§cSome items are dropped on the ground because your inventory is full!");
			}
			setCoolDown(p.getUniqueId(), k.getName(), k.getCooldown());
		} else {
			int days = (int) TimeUnit.SECONDS.toDays(cooldown);
			cooldown -= TimeUnit.DAYS.toSeconds(days);

			int hours = (int) TimeUnit.SECONDS.toHours(cooldown);
			cooldown -= TimeUnit.HOURS.toSeconds(hours);

			int minutes = (int) TimeUnit.SECONDS.toMinutes(cooldown);
			cooldown -= TimeUnit.MINUTES.toSeconds(minutes);

			int seconds = cooldown;
			
			
			p.sendMessage("§cYou already chose this kit. Wait for " + days + " days " + hours + " hours " + minutes + " minutes " + seconds + " seconds before choosing this kit.");
		}
	}
}
