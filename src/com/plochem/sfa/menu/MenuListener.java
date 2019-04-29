package com.plochem.sfa.menu;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.plochem.sfa.generator.GeneratorType;
import com.plochem.sfa.perks.PerkManager;
import com.plochem.sfa.perks.PerkType;

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
						if(name.equals("§aCosmetics")) {
							p.sendMessage("§cThis feature is coming soon!");
							p.closeInventory();
						} else if (name.equals("§aPerks")) {
							openPerkShop(p);
						} else if (name.equals("§aOre Generators")) {
							openGenShop((Player)e.getWhoClicked());
						} else if (name.equals("§aShop")) {
							Bukkit.getServer().dispatchCommand(p, "shop");
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
			genMeta.setLore(Arrays.asList("", "§7Click to purchase for §a$" + NumberFormat.getInstance().format(GeneratorType.values()[i].getCost()) + "§7!"));
			genMeta.setDisplayName("§e" + GeneratorType.values()[i].toString().replaceAll("_", " ") + " Generator");
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
			List<String> desc = Arrays.asList(type.getDescription());
			int currentLevel = PerkManager.currentLevel(type, p);
			if(currentLevel == type.getMaxLevel()) {
				desc.add("");
				desc.add("§aMaximum level reached!");
			} else if(currentLevel == 0) {
				desc.add("");
				desc.add("§7Cost: §a$" + type.getUnlockCost());
				desc.add("");
				desc.add("§eClick to purchase!");
			} else { // upgradable
				desc.add("");
				desc.add("§7Cost: §a$" + type.getUnlockCost() * Math.pow(PerkManager.PURCHASE_SCALE_FACTOR, currentLevel));
				desc.add("");
				desc.add("§eClick to upgrade to level " + currentLevel+1 + "!");
			}
			currIm.setLore(desc);
			genShop.setItem(i, curr);
			// get description, put in lore, add last line (write cost  if havent bought else wirte &aUnlocked!)
		}
		p.openInventory(genShop);
	}
}