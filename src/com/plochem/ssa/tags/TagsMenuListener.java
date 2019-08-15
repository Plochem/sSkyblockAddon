package com.plochem.ssa.tags;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class TagsMenuListener implements Listener{
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getClickedInventory() != null && e.getClickedInventory().getItem(e.getSlot()) != null) {
			if(e.getInventory().getTitle().contains("Tags")) {
				e.setCancelled(true);
				if(e.getClickedInventory().getTitle().contains("Tags")) {
					if(e.getCurrentItem().getType() == Material.NAME_TAG) {
						String identifier = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[1]);

						if(TagManager.has((Player)e.getWhoClicked(), identifier)) {
							TagManager.setTag((Player)e.getWhoClicked(), identifier);
						} else {
							e.getWhoClicked().sendMessage("§cYou do not own this tag!");
						}
						e.getWhoClicked().closeInventory();
					} else if(e.getCurrentItem().getType() == Material.ARROW) {
						int page = Integer.parseInt(e.getClickedInventory().getTitle().split(" ")[3]);
						if(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Previous page")) {
							TagManager.openMenu((Player)e.getWhoClicked(), page-1);
						} else {
							TagManager.openMenu((Player)e.getWhoClicked(), page+1);
						}
					} else if(e.getCurrentItem().getType() == Material.STAINED_GLASS){
						if(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("remove current tag")) {
							TagManager.removeTag((Player)e.getWhoClicked());
							e.getWhoClicked().closeInventory();
						}
					}


				}
			}
		}
	}

}
