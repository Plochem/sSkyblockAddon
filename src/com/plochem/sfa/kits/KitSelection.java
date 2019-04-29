package com.plochem.sfa.kits;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
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
							if(ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase(k.getName())) {
								e.getWhoClicked().closeInventory();
								if(e.getWhoClicked().hasPermission("sfa.kit" + name)) { // TODO check if actually works
									KitManager.giveKit((Player)e.getWhoClicked(), k);
								} else {
									e.getWhoClicked().sendMessage("§cYou do not have permission to use this kit! Purchase a rank to use it.");
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
