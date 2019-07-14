package com.plochem.ssa.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.plochem.ssa.SSkyblockAddon;

public class PlayerRespawn implements Listener{
	@EventHandler
	public void onDeath(PlayerRespawnEvent e) {
		if(e.getPlayer().getWorld().getName().equals("pvp")) 
			e.setRespawnLocation(SSkyblockAddon.getPlugin(SSkyblockAddon.class).getPvPSpawn());
		else 
			e.setRespawnLocation(SSkyblockAddon.getPlugin(SSkyblockAddon.class).getSpawn());
	}

}
