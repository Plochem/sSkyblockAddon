package com.plochem.ssa.gearset;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.plochem.ssa.ItemBuilder;
import com.plochem.ssa.bosses.Tier;
import com.plochem.ssa.gearset.abilities.StickyBow;

public class GearSetManager {
	private static Map<String, GearSet> gearsets = new HashMap<>();
	static {
		gearsets.put("Paladin", new GearSet(Arrays.asList(
				new ItemBuilder(Material.DIAMOND_HELMET, 1).displayname("§5§lPaladin §d§lHelmet").enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10).enchant(Enchantment.DURABILITY, 10).setNBTString("gearset", "paladin_helmet").build(),
				new ItemBuilder(Material.DIAMOND_CHESTPLATE, 1).displayname("§5§lPaladin §d§lChestplate").enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10).enchant(Enchantment.DURABILITY, 10).setNBTString("gearset", "paladin_chestplate").build(), 
				new ItemBuilder(Material.DIAMOND_LEGGINGS, 1).displayname("§5§lPaladin §d§lLeggings").enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10).enchant(Enchantment.DURABILITY, 10).setNBTString("gearset", "paladin_leggings").build(),
				new ItemBuilder(Material.DIAMOND_BOOTS, 1).displayname("§5§lPaladin §d§lBoots").enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10).enchant(Enchantment.DURABILITY, 10).setNBTString("gearset", "paladin_boots").build(), 
				new ItemBuilder(Material.BOW, 1).displayname("§5§lPaladin §d§lBow").enchant(Enchantment.ARROW_DAMAGE, 6).enchant(Enchantment.ARROW_FIRE, 2).enchant(Enchantment.ARROW_INFINITE, 1).setNBTString("gearset", "paladin_bow").build()),
				Tier.EPIC, "Paladin", new StickyBow())); 
	}
	
	public static boolean isWearingSet(GearSet set, Player p) {
		for(ItemStack item : set.getItems()) {
			if(!invContains(p.getInventory(), item)) {
				return false;
			}
		}
		return true;
	}
	
	public static GearSet getGearSet(String s) {
		return gearsets.get(s);
	}
	
	private static boolean invContains(PlayerInventory inv, ItemStack gearItem) {
		for(ItemStack armor : inv.getContents()) {
			if(new ItemBuilder(gearItem).getNBTString("gearset").equals(new ItemBuilder(armor).getNBTString("gearset"))) {
				return true;
			}
		}
		return false;
	}
}
