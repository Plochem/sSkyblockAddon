package com.plochem.ssa.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;

import com.plochem.ssa.SSkyblockAddon;

public class VoidDamageListener implements Listener{
	@EventHandler
	public void onVoid(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player && e.getEntity().getWorld().getName().equalsIgnoreCase("spawn")) {
			if(e.getCause() == DamageCause.VOID) {
				e.setCancelled(true);
				new BukkitRunnable() {
				    public void run() {
				    	e.getEntity().teleport(SSkyblockAddon.getPlugin(SSkyblockAddon.class).getSpawn());				     
				        cancel();
				    }
				}.runTaskLater(SSkyblockAddon.getPlugin(SSkyblockAddon.class), 1);
				
			}
		}
	}
}
