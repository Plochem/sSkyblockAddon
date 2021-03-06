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

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;

public class TagManager {
	private static File tagFile = new File("plugins/SFA/tags/tags.yml");
	private static YamlConfiguration tagData = YamlConfiguration.loadConfiguration(tagFile);
	private static File playerTagFile = new File("plugins/SFA/tags/playerTags.yml");
	private static YamlConfiguration playerTagData = YamlConfiguration.loadConfiguration(playerTagFile);
	private static LuckPermsApi api = LuckPerms.getApi();
	
	public static void openMenu(Player p, int page) {
		tagData = YamlConfiguration.loadConfiguration(tagFile);
		Inventory menu = Bukkit.createInventory(null, 54, "Tags - Page: " + page);
		if(tagData.getKeys(false).isEmpty()) {
			p.sendMessage("�cNo tags have been created yet!");
			return;
		}
		List<String> keys = new ArrayList<>(tagData.getKeys(false));
		for(int i = (45*page); i < Math.min(45 * (page+1), keys.size()); i++) {
			String identifier = keys.get(i).toLowerCase();
			String tag = tagData.getString(identifier + ".tag").replaceAll("&", "�");
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
				item.setType(Material.STAINED_GLASS_PANE);
				item.setDurability((short)14);
				lore.add("�cYou do not have this tag!");
				lore.add(tagData.getString(identifier + ".nothave").replaceAll("&", "�"));
			}
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			menu.addItem(item);
		}
		if(page != 0) {
			ItemStack item = new ItemStack(Material.ARROW);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName("�ePrevious page");
			item.setItemMeta(itemMeta);
			menu.setItem(48, item);
		}
		if(45 * (page+1) < keys.size()) { // checks if current page has enough space
			ItemStack item = new ItemStack(Material.ARROW);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName("�eNext page");
			item.setItemMeta(itemMeta);
			menu.setItem(50, item);
		}
		ItemStack item = new ItemStack(Material.STAINED_GLASS, 1, (short)14);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("�cRemove current tag");
		item.setItemMeta(itemMeta);
		menu.setItem(49, item);
		p.openInventory(menu);
	}
	
	public static void create(String identifier, String tag) {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.addPermission(new Permission("sfa.tags." + identifier.toLowerCase()));
		tagData.set(identifier.toLowerCase() + ".tag", tag);
		tagData.set(identifier.toLowerCase() + ".nothave", "&cPurchase it on the server store.");
		saveTagFile();
	}
	
	public static void delete(String identifier) {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.removePermission(new Permission("sfa.tags." + identifier.toLowerCase()));
		tagData.set(identifier.toLowerCase(), null);
		saveTagFile();
	}
	
	public static void giveTag(String identifier, String name) {
		Node node = api.getNodeFactory().newBuilder("sfa.tags." + identifier.toLowerCase()).build();
        User user = api.getUser(name);
        if (user != null) {
            user.setPermission(node);
            api.getUserManager().saveUser(user);

        }
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
		String tag = tagData.getString(identifier.toLowerCase() + ".tag");
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
	
	public static void removeTag(Player p) {
		playerTagData.set(p.getUniqueId().toString(), null);
		savePlayerTagFile();
		p.sendMessage("�aYour tag has been removed.");
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
