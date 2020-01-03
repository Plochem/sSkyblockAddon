package com.plochem.ssa.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import com.kiwifisher.mobstacker.MobStacker;

import me.minebuilders.clearlag.events.EntityRemoveEvent;

public class ClearLagListener implements Listener{
	@EventHandler
	public void onClear(ServerCommandEvent e) {
		if(e.getCommand().equals("/lagg clear")) {
			MobStacker.getPlugin(MobStacker.class).removeAllStacks();
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "All stacks were successfully removed");
		}
	}
	
	@EventHandler
	public void onClear(PlayerCommandPreprocessEvent e) {
		if(e.getMessage().equals("/lagg clear")) {
			MobStacker.getPlugin(MobStacker.class).removeAllStacks();
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "All stacks were successfully removed");
		}
	}
	
	@EventHandler
	public void onClear(EntityRemoveEvent e) {
		MobStacker.getPlugin(MobStacker.class).removeAllStacks();
	}

}
