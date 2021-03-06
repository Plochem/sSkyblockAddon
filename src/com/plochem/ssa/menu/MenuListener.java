package com.plochem.ssa.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.plochem.ssa.cosmetics.CosmeticManager;
import com.plochem.ssa.generator.GeneratorType;
import com.plochem.ssa.perks.PerkManager;
import com.plochem.ssa.perks.PerkType;

public class MenuListener implements Listener{
	@EventHandler
	public void menuClick(InventoryClickEvent e) {
		if(e.getClickedInventory() != null && e.getClickedInventory().getItem(e.getSlot()) != null) {
			if(e.getInventory().getTitle().equalsIgnoreCase("menu")) {
				e.setCancelled(true);
				if(e.getClickedInventory().getTitle().equalsIgnoreCase("menu")) {
					ItemStack i = e.getCurrentItem();
					ItemMeta im = i.getItemMeta();
					String name = im.getDisplayName();
					if(name != null) {
						Player p = (Player)e.getWhoClicked();
						if(name.equals("�aCosmetics")) {
							CosmeticManager.openMainMenu(p);
						} else if (name.equals("�aPerks")) {
							openPerkShop(p);
						} else if (name.equals("�aOre Generators")) {
							openGenShop((Player)e.getWhoClicked());
						} else if (name.equals("�aShop")) {
							Bukkit.getServer().dispatchCommand(p, "shop");
						} else if (name.equals("�aRewards")) {
							Bukkit.getServer().dispatchCommand(p, "rewards");
						} else if (name.equals("�aCrates")) {
							Bukkit.getServer().dispatchCommand(p, "crates");
						} else if (name.equals("�aKits")) {
							Bukkit.getServer().dispatchCommand(p, "kits");
						} else if (name.equals("�aSeasons")) {
							Bukkit.getServer().dispatchCommand(p, "season");
						}
					}

				}
			}
		}

	}
	
	private void openGenShop(Player p) {
		Inventory genShop = Bukkit.createInventory(null, 9, "Generator Shop");
		for(int i = 0; i < GeneratorType.values().length; i++) {
			ItemStack gen = new ItemStack(Material.ENDER_PORTAL_FRAME);
			ItemMeta genMeta = gen.getItemMeta();
			genMeta.setLore(Arrays.asList("", "�7Click to purchase for �a$" + String.format("%,d", GeneratorType.values()[i].getCost()) + "�7!"));
			genMeta.setDisplayName("�e" + GeneratorType.values()[i].toString().replaceAll("_", " ") + " Generator");
			gen.setItemMeta(genMeta);
			genShop.setItem(i, gen);
		}
		p.openInventory(genShop);
	}
	
	private void openPerkShop(Player p) {
		Inventory genShop = Bukkit.createInventory(null, 27, "Perk Shop");
		for(int i = 0; i < PerkType.values().length; i++) {
			PerkType type = PerkType.values()[i];
			ItemStack curr = type.getItemRep();
			ItemMeta currIm = curr.getItemMeta();
			List<String> desc = new ArrayList<String>(Arrays.asList(type.buildDescription(p)));
			int currentLevel = PerkManager.currentLevel(type, p);
			String name = WordUtils.capitalizeFully(type.toString().replaceAll("_", " ")) +  " " + Math.min(type.getMaxLevel(), currentLevel+1);
			currIm.setDisplayName("�a" + name);
			if(currentLevel == type.getMaxLevel()) {
				desc.add("");
				desc.add("�a�lFully Upgraded!");
			} else if(currentLevel == 0) { // does not own it
				desc.add("");
				desc.add("�7Cost: �a$" + String.format("%,d", type.getUnlockCost()));
				desc.add("");
				desc.add("�eClick to purchase!");
				currIm.setDisplayName("�c" + name);
			} else { // upgradeable
				desc.add("");
				desc.add("�7Cost: �a$" + String.format("%,d", type.getCost(currentLevel)));
				desc.add("");
				desc.add("�eClick to upgrade to level " + PerkManager.getNextLvlUpgrade(type, p) + "!");
			}
			currIm.setLore(desc);
			curr.setItemMeta(currIm);
			genShop.setItem(i, curr);
			// get description, put in lore, add last line (write cost  if havent bought else wirte &aUnlocked!)
		}
		p.openInventory(genShop);
	}
}