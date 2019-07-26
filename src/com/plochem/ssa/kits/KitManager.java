package com.plochem.ssa.kits;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	private static Map<UUID, Integer> cooldowns = new HashMap<>();
	
	static {
        new BukkitRunnable() {
        	int time = 0;
        	List<UUID> toRemove = new ArrayList<>();
            @Override
            public void run() {
            	for(UUID id : cooldowns.keySet()) {
            		if(cooldowns.get(id) == 0)
            			toRemove.add(id);
            		else
            			cooldowns.put(id, cooldowns.get(id) - 1);
            	}
            	for(UUID id : toRemove) {
        			cooldowns.remove(id);
            	}
            	time++;
            	if(time == 60) {
                	for(UUID id : cooldowns.keySet()) {
                		saveCoolDown(id, cooldowns.get(id));
                	}
            		time = 0;
            	}
            }
        }.runTaskTimer(SSkyblockAddon.getPlugin(SSkyblockAddon.class), 0, 20);
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
				Bukkit.getPluginManager().addPermission(new Permission("sfa.kit" + k.getName().toLowerCase()));
			}
		}
	}
	
	public static void saveCoolDown(UUID id, int time) {
		File cooldownFile = new File("plugins/SFA/kitcooldowns/" + id.toString() + ".yml");
		YamlConfiguration cooldownData = YamlConfiguration.loadConfiguration(cooldownFile);
		cooldownData.set("time", time);
		try {
			cooldownData.save(cooldownFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveKitFile() {
		kitData.set("kits", kits);
		try {
			kitData.save(kitFile);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public static void readCooldownFiles(UUID id) {
		File cooldownFile = new File("plugins/SFA/kitcooldowns/" + id.toString() + ".yml");
		YamlConfiguration cooldownData = YamlConfiguration.loadConfiguration(cooldownFile);
		int cooldown = cooldownData.getInt("time");
		if(cooldown != 0) {
			cooldowns.put(id, cooldown);
		} else {
			cooldowns.remove(id);
		}
	}
	
	public static void openMenu(Player p) {
		Inventory menu = Bukkit.createInventory(p, 9, "Kit Menu");
		for(Kit k : kits) {
			ItemStack i = k.getItemRep();
			ItemMeta im = i.getItemMeta();
			im.setDisplayName("§f" + k.getName());
			im.setLore(Arrays.asList("§eLeft click to claim the kit!", "", "§eRight click to preview the kit!"));
			i.setItemMeta(im);
			menu.addItem(k.getItemRep());
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
		if(!cooldowns.containsKey(p.getUniqueId()) || cooldowns.get(p.getUniqueId()) == 0 || p.isOp()) {
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
			cooldowns.put(p.getUniqueId(), k.getCooldown());
			saveCoolDown(p.getUniqueId(), k.getCooldown());
		} else {
			int time = cooldowns.get(p.getUniqueId());
			int days = (int) TimeUnit.SECONDS.toDays(time);
			time -= TimeUnit.DAYS.toSeconds(days);

			int hours = (int) TimeUnit.SECONDS.toHours(time);
			time -= TimeUnit.HOURS.toSeconds(hours);

			int minutes = (int) TimeUnit.SECONDS.toMinutes(time);
			time -= TimeUnit.MINUTES.toSeconds(minutes);

			int seconds = time;
			
			
			p.sendMessage("§cYou already chose a kit. Wait for " + days + " days " + hours + " hours " + minutes + " minutes " + seconds + " seconds before choosing another kit.");
		}
	}
}
