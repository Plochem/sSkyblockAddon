package com.plochem.sfa.stats;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class StatsListener implements Listener {
	@EventHandler
	public void onKill(PlayerDeathEvent e) {
		Player killer = e.getEntity().getKiller();
		Player killed = e.getEntity();
		StatsManager.addDeath(killed);
		StatsManager.showScoreboard(killed);
		if(killer != null) {
			StatsManager.addKill(killer);
			StatsManager.showScoreboard(killer);
		}

	}
}
