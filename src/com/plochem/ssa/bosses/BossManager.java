package com.plochem.ssa.bosses;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.plochem.ssa.SSkyblockAddon;
import com.plochem.ssa.bosses.skills.Skill;
import com.plochem.ssa.bosses.skills.basic.Melee;
import com.plochem.ssa.bosses.skills.special.Poison;

public class BossManager {
	private static List<BossEntity> loadedBosses = new ArrayList<>();
	private static Map<UUID, BossEntity> currBosses = new HashMap<>();
	private static File bossFile = new File("plugins/SFA/bosses/config.yml");
	private static YamlConfiguration bossConfig = YamlConfiguration.loadConfiguration(bossFile);
	public static String prefix = "§b§lBOSS: ";
	private static Map<String, Skill> skillRegistry = new HashMap<>();
	
	static {
		skillRegistry.put("melee", new Melee());
		skillRegistry.put("poison", new Poison());
		
		loadedBosses.add(new BossEntity("§d§lPaladin", "SPIDER", 
				Arrays.asList(skillRegistry.get("poison")), 
				Arrays.asList(skillRegistry.get("melee")),
				Arrays.asList("is gay", "", "very"),
				new BossReward(Arrays.asList(
							Arrays.asList("broadcast test"), 
							Arrays.asList("cc give p straxidus 1 %player%"),
							Arrays.asList("randbal %player% 10000 50000")), 
						Arrays.asList("randbal %player% 1000 3000")), 
				new BossStatistics(15000, 4, 2, 5, 10, 3, 15, 3)));
		Bukkit.getLogger().info("[SFA] Bosses loaded!");
	}
	
	public static World getWorld() {
		return getLoc().getWorld();
	}
	
	public static Location getLoc() {
		return (Location)bossConfig.get("spawn");
	}
	
	public static Map<UUID, BossEntity> getCurrBosses(){
		return currBosses;
	}
	
	public static void createFiles() {
		if(!bossFile.exists()) {
			Bukkit.getServer().getLogger().info("[SFA] Creating bosses config file!");
			try {
				bossConfig.set("spawn", new Location(Bukkit.getWorlds().get(0), 0, 0, 0));
				bossConfig.save(bossFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}  else {
			Bukkit.getServer().getLogger().info("[SFA] Bosses config file already exists! Skipping creation...");
		}
	}
	
	public static void autospawnTimer() {
		new BukkitRunnable() {
			@Override
			public void run() {
				if(loadedBosses.size() != 0) {
				    Random rand = new Random();
				    BossEntity boss = loadedBosses.get(rand.nextInt(loadedBosses.size()));
				    ((BossEntity)boss.clone()).spawn();
				}
			}
		}.runTaskTimer(SSkyblockAddon.getPlugin(SSkyblockAddon.class), 20*3600*3, 20*3600*3); // last num = # of hours
	}
	
	public static void reload() {
		bossConfig = YamlConfiguration.loadConfiguration(bossFile);
	}
	
	public static void sendMessage(String msg) {
		for(Player p : getWorld().getPlayers()) {
			p.sendMessage(msg);
		}
	}
	
	public static List<BossEntity> getLoadBosses(){
		return loadedBosses;
	}
	

}
