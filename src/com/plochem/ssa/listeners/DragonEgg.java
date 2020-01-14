package com.plochem.ssa.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class DragonEgg implements Listener{
	@EventHandler
	public void onTp(BlockFromToEvent e) {
		if(e.getBlock().getType() == Material.DRAGON_EGG) e.setCancelled(true);
	}

}
