package com.plochem.ssa.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.minebuilders.clearlag.events.EntityRemoveEvent;

public class ClearLagListener implements Listener{
	@EventHandler
	public void onClear(EntityRemoveEvent e) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mobstacker killall");
	}

}
