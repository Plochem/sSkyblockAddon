package com.plochem.ssa.tags;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class TagsMenuListener implements Listener{
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getClickedInventory() != null && e.getClickedInventory().getItem(e.getSlot()) != null) {
			if(e.getInventory().getTitle().equalsIgnoreCase("tags")) {
				e.setCancelled(true);
				if(e.getClickedInventory().getTitle().equalsIgnoreCase("tags")) {
					String identifier = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[1]);
					if(TagManager.has((Player)e.getWhoClicked(), identifier)) {
						TagManager.setTag((Player)e.getWhoClicked(), identifier);
					} else {
						e.getWhoClicked().sendMessage("§cYou do not have this tag! Purchase them at the server store.");
					}
					e.getWhoClicked().closeInventory();
				}
			}
		}
	}

}
