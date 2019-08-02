package com.plochem.ssa.sellchest;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SellChestManager {
	
	public static void giveItem(Player target, int amt) {
		ItemStack sell = new ItemStack(Material.BLAZE_ROD, amt);
		ItemMeta sellMeta = sell.getItemMeta();
		sellMeta.setDisplayName("§eSell-Chest Wand");
		sellMeta.setLore(Arrays.asList("§7Right click on a chest to sell"));
		sell.setItemMeta(sellMeta);
		target.getInventory().addItem(sell);
		target.sendMessage("§aYou received " + amt + " sell-chest wands!");
		
	}

}
