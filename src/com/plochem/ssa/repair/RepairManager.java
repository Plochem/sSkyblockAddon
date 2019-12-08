package com.plochem.ssa.repair;

import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;



public class RepairManager {
	private static File repairFile =  new File("plugins/SFA/repair.yml");
	private static YamlConfiguration repairData = YamlConfiguration.loadConfiguration(repairFile);

	public static void createRepairFile() {
		if(!(repairFile.exists())) {
			Bukkit.getServer().getLogger().info("[SFA] Creating repair storage file!");
			saveRepairFile();
		}  else {
			Bukkit.getServer().getLogger().info("[SFA] Repair storage file already exists! Skipping creation...");
		}
	}

	private static void saveRepairFile() {
		try {
			repairData.save(repairFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addRepair(Player p) {
		repairData.set(p.getUniqueId().toString(), repairData.getInt(p.getUniqueId().toString())+1);
		saveRepairFile();
	}

	public static boolean canRepair(Player p) {
		int timesRepaired = repairData.getInt(p.getUniqueId().toString());
		if(p.hasPermission("sfa.repair.unlimited")) {
			return true;
		} else if(p.hasPermission("sfa.repair.15")) {
			if(timesRepaired < 15) {
				return true;
			} else {
				return false;
			}
		} else if(p.hasPermission("sfa.repair.10")) {
			if(timesRepaired < 10) {
				return true;
			} else {
				return false;
			}
		} else if(p.hasPermission("sfa.repair.5")) {
			if(timesRepaired < 5) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public static void startTimer() {		
		LocalDateTime tomorrowMidnight = LocalDate.now().plusDays(1).atStartOfDay();
		ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
		long midnight = LocalDateTime.now().until(tomorrowMidnight, ChronoUnit.SECONDS);
		schedule.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if(LocalDate.now().getDayOfWeek() == DayOfWeek.SUNDAY) { // runs every sunday at 23:59:59
					for(String id : repairData.getKeys(false)) {
						repairData.set(id, null);
					}
					saveRepairFile();
				}
			}
		}, midnight, TimeUnit.DAYS.toSeconds(1), TimeUnit.SECONDS);

	}

}
