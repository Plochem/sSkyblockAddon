package com.plochem.sfa.kits;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class KitPreview implements Listener{
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getClickedInventory() != null && e.getClickedInventory().getItem(e.getSlot()) != null) {
			if(e.getInventory().getTitle().contains("Previewing") || e.getClickedInventory().getTitle().contains("Previewing")) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if(e.getInventory().getTitle().contains("Previewing")) {
			KitManager.openMenu((Player)e.getPlayer());
		}
	}

}
