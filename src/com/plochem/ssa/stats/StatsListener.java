package com.plochem.ssa.stats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.events.IslandJoinEvent;
import com.wasteofplastic.askyblock.events.IslandLeaveEvent;
import com.wasteofplastic.askyblock.events.IslandNewEvent;
import com.wasteofplastic.askyblock.events.IslandPostLevelEvent;

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
	
	@EventHandler
	public void onIslandJoin(IslandJoinEvent e) {
		ASkyBlockAPI.getInstance().calculateIslandLevel(e.getIslandOwner());
	}
	
	@EventHandler
	public void onIslandLeave(IslandLeaveEvent e) {
		StatsManager.showScoreboard(Bukkit.getPlayer(e.getPlayer()));
	}
	
	@EventHandler
	public void onIslandCreate(IslandNewEvent e) {
		StatsManager.showScoreboard(e.getPlayer());
	}
	
	@EventHandler
	public void onIslandCreate(IslandPostLevelEvent e) {
		for(UUID id : e.getIsland().getMembers()) {
			StatsManager.showScoreboard(Bukkit.getPlayer(id));
		}
		StatsManager.showScoreboard(Bukkit.getPlayer(e.getIslandOwner()));

	}

	
}
