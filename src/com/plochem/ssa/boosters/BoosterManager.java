package com.plochem.ssa.boosters;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

public class BoosterManager {
	private static List<Booster> moneyBoosters = new ArrayList<>();
	private static List<Booster> expBoosters = new ArrayList<>();
	private static File boosterFile = new File("plugins/SFA/boosterqueue.yml");
	private static YamlConfiguration boosterData = YamlConfiguration.loadConfiguration(boosterFile);
	private static Countdown moneyCountdown;
	private static Countdown expCountdown;
	private static boolean moneyBoosterIsActive = false;
	private static boolean expBoosterIsActive = false;

	public static List<Booster> getMoneyBoosters() {
		return moneyBoosters;
	}

	public static List<Booster> getExpBoosters() {
		return expBoosters;
	}

	@SuppressWarnings("unchecked")
	public static void createQueueFile() {
		if(!(boosterFile.exists())) {
			Bukkit.getServer().getLogger().info("[SFA] Creating booster queue storage file!");
			saveBoosterQueue();
		}  else {
			Bukkit.getServer().getLogger().info("[SFA] Booster queue storage file already exists! Skipping creation...");
			moneyBoosters = ((List<Booster>)boosterData.getList("moneyBoosters"));
			expBoosters = ((List<Booster>)boosterData.getList("expBoosters"));
		}
	}

	public static void addBooster(Booster booster) {
		if(booster.getType() == BoosterType.MONEY) {
			moneyBoosters.add(booster);
			if(moneyBoosters.size() == 1) activate(BoosterType.MONEY);
		} else {
			expBoosters.add(booster);
			if(expBoosters.size() == 1) activate(BoosterType.EXPERIENCE);
		}
		saveBoosterQueue();
	}

	public static void activate(BoosterType type) {
		Booster booster = null;
		if(type == BoosterType.EXPERIENCE && !expBoosters.isEmpty()) {
			booster = expBoosters.get(0);
			expCountdown = new Countdown();
			expCountdown.start(booster);
			expBoosterIsActive = true;
		} else if(type == BoosterType.MONEY && !moneyBoosters.isEmpty()){
			booster = moneyBoosters.get(0);
			moneyCountdown = new Countdown();
			moneyCountdown.start(booster);
			moneyBoosterIsActive = true;
		}
		
	}

	public static void pause() {

	}
	
	public static void remove(BoosterType type) {
		if(type == BoosterType.EXPERIENCE) {
			expBoosters.remove(0);
			expBoosterIsActive = false;
		} else if(type == BoosterType.MONEY){
			moneyBoosters.remove(0);
			moneyBoosterIsActive = false;
		}
		saveBoosterQueue();
		activate(type);
	}

	public static void saveBoosterQueue() {
		boosterData.set("moneyBoosters", moneyBoosters);
		boosterData.set("expBoosters", expBoosters);
		try {
			boosterData.save(boosterFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void claimBooster(Booster booster, String color, int amount) {
		File playerFile = new File("plugins/SFA/playerBoosters/" + booster.getUUID().toString() + ".yml");
		YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
		List<Booster> playerBoosters = ((List<Booster>)playerData.getList("boosters"));
		for(int i = 0; i < amount; i++) {
			playerBoosters.add(booster);
		}
		playerData.set("boosters", playerBoosters);
		BoosterManager.savePlayerBooster(playerData, playerFile);
		Bukkit.getPlayer(booster.getUUID()).sendMessage("§aYou have received " + amount + " " + color + booster.getType().toString().toLowerCase() + " §abooster(s)!");
	}

	public static void savePlayerBooster(YamlConfiguration c, File f) {
		try {
			c.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean moneyBoosterIsActive() {
		return moneyBoosterIsActive;
	}
	
	public static boolean expBoosterIsActive() {
		return expBoosterIsActive;
	}

}
