package com.plochem.ssa.kits;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class KitSelection implements Listener{
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getClickedInventory() != null && e.getClickedInventory().getItem(e.getSlot()) != null) {
			if(e.getInventory().getTitle().equalsIgnoreCase("kit menu")) {
				e.setCancelled(true);
				if(e.getClickedInventory().getTitle().equalsIgnoreCase("kit menu")) {
					ItemStack item = e.getCurrentItem();
					if(item.getItemMeta().getDisplayName() != null) {
						for(Kit k : KitManager.getKits()) {
							String name = ChatColor.stripColor(item.getItemMeta().getDisplayName()).toLowerCase();
							if(name.equalsIgnoreCase(k.getName())) { // found corresponding kit
								e.getWhoClicked().closeInventory();
								if(e.getAction() == InventoryAction.PICKUP_HALF) { // right click item => preview kit
									Inventory preview = Bukkit.createInventory(null,54,"Previewing the " + k.getName() + " Kit");
									for(ItemStack kitItem : k.getItems()) {
										preview.addItem(kitItem);
									}
									e.getWhoClicked().openInventory(preview);
								} else { // give kit
									if(e.getWhoClicked().hasPermission("sfa.kits." + name)) {
										KitManager.giveKit((Player)e.getWhoClicked(), k);
									} else {
										e.getWhoClicked().sendMessage("§cYou do not have permission to use this kit! Purchase a rank to use it.");
									}
								}
								break;
							}
						}

					}
				}
			}
		}
	}


}
