package com.plochem.sfa.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.plochem.sfa.SFactionAddon;

public class PlayerRespawn implements Listener{
	@EventHandler
	public void onDeath(PlayerRespawnEvent e) {
		e.setRespawnLocation(SFactionAddon.getPlugin(SFactionAddon.class).getSpawn());
	}

}
