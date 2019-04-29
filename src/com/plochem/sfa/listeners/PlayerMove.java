package com.plochem.sfa.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.plochem.sfa.SFactionAddon;

public class PlayerMove implements Listener{
	SFactionAddon sfa = SFactionAddon.getPlugin(SFactionAddon.class);
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(e.getFrom().getX() != e.getTo().getX() && e.getFrom().getZ() != e.getTo().getZ()) {
			if(sfa.getWhoRunningSomeTPCmd().contains(e.getPlayer().getUniqueId())) {
				sfa.getWhoRunningSomeTPCmd().remove(e.getPlayer().getUniqueId());
			}
		}
	}

}
