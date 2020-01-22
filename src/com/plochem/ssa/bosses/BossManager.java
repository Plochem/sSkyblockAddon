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
import com.plochem.ssa.bosses.skills.special.Cobwebs;
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
		skillRegistry.put("cobwebs", new Cobwebs());
		loadedBosses.add(new BossEntity("Paladin", "SPIDER", Tier.EPIC,
				Arrays.asList(skillRegistry.get("poison"), skillRegistry.get("cobwebs")), 
				Arrays.asList(skillRegistry.get("melee")),
				Arrays.asList("§7A dangerous spider that can easily", "defeat anything in its path."),
				new BossReward(Arrays.asList(
						Arrays.asList("cc give p straxidus 1 %player%", "randbal %player% 100000 300000", "gearset give %player% Paladin"), 
						Arrays.asList("cc give p mythical 1 %player%", "randbal %player% 50000 75000"),
						Arrays.asList("randbal %player% 10000 30000")), 
						Arrays.asList("randbal %player% 1000 3000")), 
				new BossStatistics(15000, 4, 2, 5, 10, 5, 10, 2.5)));
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
				bossConfig.set("maxalive", 1);
				bossConfig.set("minplayers", 7);
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
			int cnt = 0;
			@Override
			public void run() {
				if(cnt == 120) {
					Bukkit.broadcastMessage(prefix + "§7A boss is spawning in §c1 hour!");
				} else if(cnt == 150) {
					Bukkit.broadcastMessage(prefix + "§7A boss is spawning in §c30 minutes!");
				} else if(cnt == 170) {
					Bukkit.broadcastMessage(prefix + "§7A boss is spawning in §c10 minutes!");
				}else if(cnt == 175) {
					Bukkit.broadcastMessage(prefix + "§7A boss is spawning in §c5 minutes!");
				}else if(cnt == 179) {
					Bukkit.broadcastMessage(prefix + "§7A boss is spawning in §c1 minute!");
				}else if(cnt == 180) {
					if(loadedBosses.size() != 0) {
						if(Bukkit.getServer().getOnlinePlayers().size() >= bossConfig.getInt("minplayers")) {
							if(currBosses.size() < bossConfig.getInt("maxalive")) {
								Random rand = new Random();
								BossEntity boss = loadedBosses.get(rand.nextInt(loadedBosses.size()));
								((BossEntity)boss.clone()).spawn();
							} else {
								Bukkit.broadcastMessage(prefix + "§7There are too many bosses spawned.");		
							}
						} else {
							Bukkit.broadcastMessage(prefix + "§7There are not enough players online.");							
						}
					} else {
						Bukkit.broadcastMessage(prefix + "§7There are no loaded bosses to spawn.");
					}
					cnt = -1;
				}
				cnt++;
			}
		}.runTaskTimer(SSkyblockAddon.getPlugin(SSkyblockAddon.class), 0, 20*60); // run every min
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

	public static void despawnAll() {
		for(BossEntity boss : currBosses.values()) {
			boss.getEntity().remove();
		}
		currBosses.clear();
		
	}


}
