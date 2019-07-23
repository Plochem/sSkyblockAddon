package com.plochem.ssa.tags;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

public class TagManager {
	private static File tagFile = new File("plugins/sfa/tags/tags.yml");
	private static YamlConfiguration tagData = YamlConfiguration.loadConfiguration(tagFile);
	private static File playerTagFile = new File("plugins/sfa/tags/playerTags.yml");
	private static YamlConfiguration playerTagData = YamlConfiguration.loadConfiguration(playerTagFile);
	public static void openMenu(Player p) {
		Inventory menu = Bukkit.createInventory(null, 54, "Tags");
		if(tagData.getKeys(false).isEmpty()) {
			p.sendMessage("�cNo tags have been created yet!");
			return;
		}
		for(String identifier : tagData.getKeys(false)) {
			identifier = identifier.toLowerCase();
			String tag = tagData.getString(identifier).replaceAll("&", "�");
			ItemStack item = new ItemStack(Material.NAME_TAG);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName("�6Tag�f:�6 " + StringUtils.capitalize(identifier));
			List<String> lore = new ArrayList<>();
			lore.add("�7Gives you the");
			lore.add(tag);
			lore.add("�7tag in chat.");
			lore.add("");
			if(has(p, identifier)) {
				lore.add("�eClick to select!");
			} else {
				lore.add("�cYou do not have this tag!");
				lore.add("�cPurchase them at the server store.");
			}
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			menu.addItem(item);
		}
		p.openInventory(menu);
	}
	
	public static void create(String identifier, String tag) {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.addPermission(new Permission("sfa.tags." + identifier.toLowerCase()));
		tagData.set(identifier.toLowerCase(), tag);
		saveTagFile();
	}
	
	public static void delete(String identifier) {
		tagData.set(identifier.toLowerCase(), null);
		saveTagFile();
	}
	
	public static boolean exists(String identifier) {
		if(tagData.getString(identifier.toLowerCase()) == null)
			return false;
		return true;
	}
	
	public static String getTag(Player p) {
		String identifier = playerTagData.getString(p.getUniqueId().toString());
		if(identifier == null) {
			return "";
		}
		String tag = tagData.getString(identifier.toLowerCase());
		if(tag == null) {
			playerTagData.set(p.getUniqueId().toString(), null);
			savePlayerTagFile();
			return "";
		}
		return " " + tag.replaceAll("&", "�");
	}
	
	public static void setTag(Player p, String identifier) {
		if(tagData.getString(identifier.toLowerCase()) != null) {
			playerTagData.set(p.getUniqueId().toString(), identifier.toLowerCase());
			savePlayerTagFile();
			p.sendMessage("�aYour tag is now" + getTag(p));
		} else {
			p.sendMessage("�cThat tag does not seem to exist anymore.");
		}

	}

	public static void createTagFiles() {
		if(!(tagFile.exists())) {
			Bukkit.getServer().getLogger().info("[SFA] Creating tags file!");
			saveTagFile();
		}  else {
			Bukkit.getServer().getLogger().info("[SFA] Tags file already exists! Skipping creation...");
		}
		if(!(playerTagFile.exists())) {
			Bukkit.getServer().getLogger().info("[SFA] Creating player tags file!");
			savePlayerTagFile();
		}  else {
			Bukkit.getServer().getLogger().info("[SFA] Player tags file already exists! Skipping creation...");
		}
		
	}
	
	public static void saveTagFile() {
		try {
			tagData.save(tagFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void savePlayerTagFile() {
		try {
			playerTagData.save(playerTagFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean has(Player p, String identifier) {
		return p.hasPermission("sfa.tags." + identifier.toLowerCase());
	}

}