package com.plochem.ssa.bosses;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import com.plochem.ssa.SSkyblockAddon;

public class BossManager {
	private static List<BossEntity> loadedBosses = new ArrayList<>();
	private static Map<UUID, BossEntity> currBosses = new HashMap<>();
	private static File bossFile = new File("plugins/SFA/bosses/config.yml");
	private static YamlConfiguration bossConfig = YamlConfiguration.loadConfiguration(bossFile);
	
	public static String getWorld() {
		return getLoc().getWorld().getName();
	}
	
	public static Location getLoc() {
		return (Location)bossConfig.get("spawn");
	}
	
	public static Map<UUID, BossEntity> getCurrBosses(){
		return currBosses;
	}
	
	public static void autospawnTimer() {
		new BukkitRunnable() {
			@Override
			public void run() {
			    Random rand = new Random();
			    BossEntity boss = loadedBosses.get(rand.nextInt(loadedBosses.size()));
			    boss.spawn();
			}
		}.runTaskTimer(SSkyblockAddon.getPlugin(SSkyblockAddon.class), 0, 20*3600*3); // last num = # of hours
	}
}
