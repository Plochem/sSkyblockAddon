package com.plochem.ssa.stats;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.massivecraft.factions.event.FPlayerJoinEvent;
import com.massivecraft.factions.event.FPlayerLeaveEvent;
import com.massivecraft.factions.event.FactionCreateEvent;
import com.massivecraft.factions.event.FactionDisbandEvent;

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
	public void onFactionJoin(FPlayerJoinEvent e) {
		StatsManager.showScoreboard(e.getfPlayer().getPlayer());
	}
	
	@EventHandler
	public void onFactionLeave(FPlayerLeaveEvent e) {
		StatsManager.showScoreboard(e.getfPlayer().getPlayer());
	}
	
	@EventHandler
	public void onFactionCreate(FactionCreateEvent e) {
		StatsManager.showScoreboard(e.getFPlayer().getPlayer());
	}
	
	@EventHandler
	public void onFactionDisband(FactionDisbandEvent e) {
		StatsManager.showScoreboard(e.getFPlayer().getPlayer());
	}
}
