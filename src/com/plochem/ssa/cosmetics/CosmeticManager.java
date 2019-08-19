package com.plochem.ssa.cosmetics;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

import com.plochem.ssa.cosmetics.types.Cosmetic;
import com.plochem.ssa.cosmetics.types.ProjectileTrail;
import com.plochem.ssa.cosmetics.types.TrailEffect;

public class CosmeticManager {

	public static Map<UUID, ProjectileTrail> projectile = new HashMap<>();
	public static Map<UUID, TrailEffect> trail = new HashMap<>();
	
	
	public static void createConfig() {
		File f = new File("plugins/SFA/cosmetics/cosmetics.yml");
		YamlConfiguration c = YamlConfiguration.loadConfiguration(f);		
		if(!(f.exists())) {
			Bukkit.getServer().getLogger().info("[SFA] Creating cosmetics config file!");
			for(CosmeticType type : CosmeticType.values()) {
				for(Cosmetic cos : getCosmetics(type)) {
					c.set(type.toString() + "." + cos.toString(), "Purchase this at the server store.");
				}
			}
		}  else {
			Bukkit.getServer().getLogger().info("[SFA] Cosmetics config file already exists! Skipping creation...");
		}
		save(c, f);		
	}
	
	public static void saveSelections() {
		for(UUID id : projectile.keySet()) {
			File f = new File("plugins/SFA/cosmetics/playerdata/" + id.toString() + ".yml");
			YamlConfiguration c = YamlConfiguration.loadConfiguration(f);
			c.set(CosmeticType.Projectile_Trail.toString(), projectile.get(id).toString());
			save(c, f);
		}
		
		for(UUID id : trail.keySet()) {
			File f = new File("plugins/SFA/cosmetics/playerdata/" + id.toString() + ".yml");
			YamlConfiguration c = YamlConfiguration.loadConfiguration(f);
			c.set(CosmeticType.Trail_Effect.toString(), trail.get(id).toString());
			save(c, f);
		}
		//
	}
	
	private static void save(YamlConfiguration c, File f) {
		try {
			c.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void registerPerms() {
		PluginManager pm = Bukkit.getPluginManager();
		for(CosmeticType type : CosmeticType.values()) {
			for(Cosmetic c : getCosmetics(type)) {
				pm.addPermission(new Permission("sfa.cosmetics." + type.toString() + "." + c.toString()));
			}
		}
		//
	}

	public static void openShop(Player p, CosmeticType type) {
		Inventory menu = Bukkit.createInventory(null, 54, type.toString().replaceAll("_", " "));
		File f = new File("plugins/SFA/cosmetics/playerdata/" + p.getUniqueId().toString() + ".yml");
		YamlConfiguration c = YamlConfiguration.loadConfiguration(f);
		String shopName = type.toString().replaceAll("_", " ").toLowerCase();
		Cosmetic[] cosmetics = getCosmetics(type);
		for(Cosmetic cos : cosmetics) { //
			ItemStack item = new ItemStack(cos.getMaterial());
			ItemMeta im = item.getItemMeta();
			String itemName = cos.toString();
			String itemDisplayName = cos.toString().replaceAll("_", " ");
			if(p.hasPermission("sfa.cosmetics." + type.toString() + "." + itemName)){  
				im.setDisplayName("§a" + itemDisplayName);
				if(c.getString(type.toString()).equals(itemName)){
					im.setLore(Arrays.asList("§7Selects the " + itemDisplayName, "§7" + shopName + ".", "", "§aSelected!"));
				} else {
					im.setLore(Arrays.asList("§7Selects the " + itemDisplayName , "§7" + shopName + ".", "", "§eClick to select!"));
				}
			} else {
				im.setDisplayName("§c" + itemDisplayName);
				im.setLore(Arrays.asList("§7Selects the " + itemDisplayName , "§7" + shopName + ".", "", "§c" + getObtainMethod(type.toString(), itemName)));
			}
			item.setItemMeta(im);
			menu.addItem(item);
		}
		p.openInventory(menu);

	}

	public static String getObtainMethod(String cosmeticType, String itemName) {
		File f = new File("plugins/SFA/cosmetics/cosmetics.yml");
		YamlConfiguration c = YamlConfiguration.loadConfiguration(f);
		return c.getString(cosmeticType + "." + itemName);
	}
	
	public static Cosmetic[] getCosmetics(CosmeticType type) {
		if(type == CosmeticType.Projectile_Trail) {
			return ProjectileTrail.values();
		} else if(type == CosmeticType.Trail_Effect) {
			return TrailEffect.values();
		}
		return null;
	}

	public static void openMainMenu(Player p) {
		Inventory shop = Bukkit.createInventory(null, 54, "Cosmetics");
		ItemStack projectileTrailItem = new ItemStack(Material.ARROW);
		ItemStack killEffectItem = new ItemStack(Material.BONE);
		ItemStack deathSoundItem = new ItemStack(Material.GHAST_TEAR);
		ItemStack killMessageItem = new ItemStack(Material.SIGN);
		ItemStack trailsItem = new ItemStack(Material.STRING);
		ItemStack lvlDisplayItem = new ItemStack(Material.NAME_TAG);
		ItemStack close = new ItemStack(Material.BARRIER);

		ItemMeta im = projectileTrailItem.getItemMeta();
		im.setDisplayName("§aProjectile Trails");
		im.setLore(Arrays.asList("§7Change your projectile trail effect.", "", "§eClick to view!"));
		projectileTrailItem.setItemMeta(im);

		im = killEffectItem.getItemMeta();
		im.setDisplayName("§aKill Effects");
		im.setLore(Arrays.asList("§7Pick an effect to display after", "§7eliminating another player.", "", "§eClick to view!"));
		killEffectItem.setItemMeta(im);

		im = deathSoundItem.getItemMeta();
		im.setDisplayName("§aDeath Sounds");
		im.setLore(Arrays.asList("§7Select a sound to play whenever", "§7you die.", "", "§eClick to view!"));
		deathSoundItem.setItemMeta(im);

		im = killMessageItem.getItemMeta();
		im.setDisplayName("§aKill Messages");
		im.setLore(Arrays.asList("§7Choose a kill message package", "§7to replace the default message.", "", "§eClick to view!"));
		killMessageItem.setItemMeta(im);

		im = trailsItem.getItemMeta();
		im.setDisplayName("§aTrail Effects");
		im.setLore(Arrays.asList("§7Select your trail effect that is", "§7played whenever you move." , "", "§eClick to view!"));
		trailsItem.setItemMeta(im);

		im = lvlDisplayItem.getItemMeta();
		im.setDisplayName("§aLevel Colors");
		im.setLore(Arrays.asList("§7Change the color of your", "§7level suffix.", "", "§cOnly for players who have a rank!", "", "§eClick to view!"));
		lvlDisplayItem.setItemMeta(im);

		im = close.getItemMeta();
		im.setDisplayName("§cClose");
		close.setItemMeta(im);

		shop.setItem(10, projectileTrailItem);
		shop.setItem(12, killEffectItem);
		shop.setItem(14, deathSoundItem);
		shop.setItem(16, killMessageItem); 
		shop.setItem(29, trailsItem);
		shop.setItem(31, lvlDisplayItem); 
		shop.setItem(49, close);

	}
}
