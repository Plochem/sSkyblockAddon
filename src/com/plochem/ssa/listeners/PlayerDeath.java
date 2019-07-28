package com.plochem.ssa.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener{
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if(e.getEntity().hasPermission("sfa.keepexp"))
			e.setKeepLevel(true);
	}

}
