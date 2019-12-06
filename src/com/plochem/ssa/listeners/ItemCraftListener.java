package com.plochem.ssa.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class ItemCraftListener implements Listener{
	@EventHandler
	public void onCraft(CraftItemEvent e) {
		if(e.getRecipe().getResult().getType() == Material.HOPPER) {
			e.setCancelled(true);
		}
	}

}
