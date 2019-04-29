package com.plochem.sfa.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.plochem.sfa.SFactionAddon;

public class InvseeEdit implements Listener{
	SFactionAddon sfa = SFactionAddon.getPlugin(SFactionAddon.class);
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(!e.getWhoClicked().hasPermission("sfa.invsee.edit")) {
			if(sfa.getViewingInv().contains(e.getWhoClicked().getUniqueId())) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		sfa.getViewingInv().remove(e.getPlayer().getUniqueId());
	}

}
