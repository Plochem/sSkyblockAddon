package com.plochem.ssa.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.plochem.ssa.SSkyblockAddon;

public class PlayerMove implements Listener{
	SSkyblockAddon sfa = SSkyblockAddon.getPlugin(SSkyblockAddon.class);
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(e.getFrom().getX() != e.getTo().getX() && e.getFrom().getZ() != e.getTo().getZ()) {
			if(sfa.getWhoRunningSomeTPCmd().contains(e.getPlayer().getUniqueId())) {
				sfa.getWhoRunningSomeTPCmd().remove(e.getPlayer().getUniqueId());
			}
		}
	}

}
