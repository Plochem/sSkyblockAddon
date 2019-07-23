package com.plochem.ssa.listeners;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.plochem.ssa.SSkyblockAddon;


import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;


public class EventsCancelInSpawn implements Listener{
	private List<String> worldNames = Arrays.asList("spawn", "pvp", "mob");
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager().getWorld().getName().equalsIgnoreCase("spawn") && e.getEntity().getWorld().getName().equalsIgnoreCase("spawn")) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player && e.getEntity().getWorld().getName().equalsIgnoreCase("spawn")) {
			if(e.getCause() == DamageCause.VOID) {
				((Player)e.getEntity()).teleport(SSkyblockAddon.getPlugin(SSkyblockAddon.class).getSpawn());
			}
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if(worldNames.contains(e.getPlayer().getWorld().getName())) {
			if(!e.getPlayer().hasPermission("sfa.editspawn")) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if(worldNames.contains(e.getPlayer().getWorld().getName())) {
			if(!e.getPlayer().hasPermission("sfa.editspawn")) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent e) {
		if(worldNames.contains(e.getLocation().getWorld().getName())) {
			e.setCancelled(true);
		}
	}
	
}
