package com.plochem.ssa.kits;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.plochem.ssa.SSkyblockAddon;

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
			new BukkitRunnable() { // need 1 tick delay because of stackoverflow
				@Override
				public void run () {
					KitManager.openMenu((Player)e.getPlayer());
				}
			}.runTaskLater(SSkyblockAddon.getPlugin(SSkyblockAddon.class), 1);         
		}
	}
}


