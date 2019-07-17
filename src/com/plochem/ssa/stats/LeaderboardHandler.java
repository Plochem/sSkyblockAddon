package com.plochem.ssa.stats;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitScheduler;

import com.plochem.ssa.SSkyblockAddon;

public class LeaderboardHandler {
	private static List<Leaderboard> holograms = new ArrayList<>();

	public static void createLeaderboards(YamlConfiguration c){
		Leaderboard killLB = new Leaderboard((Location)c.get("killLb"), "Total Kills", organizeData("kills"));
		killLB.show();
		holograms.add(killLB);
		Leaderboard deathLB = new Leaderboard((Location)c.get("deathLb"), "Total Deaths", organizeData("deaths"));
		deathLB.show();
		holograms.add(deathLB);
	}
	
	public static void update(SSkyblockAddon ssa, YamlConfiguration c) {
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		 scheduler.scheduleSyncRepeatingTask(ssa, new Runnable() {
			@Override
			public void run() {
				deleteLeaderboards();
				createLeaderboards(c);
			}
		}, 0L, 300L); // 60*1200
	}
	
	public static void deleteLeaderboards() {
		for(Leaderboard holo : holograms) {
			holo.delete();
		}
	}

	private static List<String> organizeData(String type){
		Map<String, Integer> data = new HashMap<>();
		File folder = new File("plugins/SFA/playerStats");
		File[] files = folder.listFiles();
		for(File f : files) {
			YamlConfiguration c = YamlConfiguration.loadConfiguration(f);
			data.put(Bukkit.getOfflinePlayer(UUID.fromString(f.getName().replaceAll(".yml", ""))).getName(), c.getInt(type));
		}
        Set<Entry<String, Integer>> set = data.entrySet();
        List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(set);
        Collections.sort( list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 ){
                return (o2.getValue()).compareTo( o1.getValue());
            }
        });
		List<String> lines = new ArrayList<>();
		for(int i = 0; i < Math.min(10, list.size()); i++) {
			Entry<String, Integer> entry = list.get(i);
			if(entry.getKey() != null) {	
				lines.add("§e" + (i+1) + ". " +  "§6" + entry.getKey() + " §7- §e" + entry.getValue());
			}

		}
        return lines;
	}
}
