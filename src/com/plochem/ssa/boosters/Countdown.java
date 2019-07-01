package com.plochem.ssa.boosters;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

import com.plochem.ssa.SSkyblockAddon;

public class Countdown {
	
	private int countdown;
	private String color = "§3";
	
	public void start(Booster booster) {
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		if(booster.getType() == BoosterType.MONEY) color = "§6";
		Bukkit.broadcastMessage("§c[§e!§c]§a " + Bukkit.getOfflinePlayer(booster.getUUID()).getName() + "'s " + color + booster.getType().toString().toLowerCase() + " §abooster has activated!");
		countdown = scheduler.scheduleSyncRepeatingTask(SSkyblockAddon.getPlugin(SSkyblockAddon.class), new Runnable() {
			int time = booster.getTimeLeft();
			public void run() {
				if(time == 0) {
					Bukkit.broadcastMessage("§c[§e!§c] " + Bukkit.getOfflinePlayer(booster.getUUID()).getName() + "'s " + color + booster.getType().toString().toLowerCase() + " §cbooster has expired!");
					BoosterManager.remove(booster.getType());
					Bukkit.getScheduler().cancelTask(countdown);
				}
				booster.setTimeLeft(time);
				BoosterManager.saveBoosterQueue();
				time--;
			}
		}, 0L, 1200L);
	}
}
