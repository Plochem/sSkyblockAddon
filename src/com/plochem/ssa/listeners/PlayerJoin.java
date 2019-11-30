package com.plochem.ssa.listeners;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.plochem.ssa.SSkyblockAddon;
import com.plochem.ssa.boosters.Booster;
import com.plochem.ssa.cosmetics.CosmeticManager;
import com.plochem.ssa.cosmetics.CosmeticType;
import com.plochem.ssa.cosmetics.types.ProjectileTrail;
import com.plochem.ssa.cosmetics.types.TrailEffect;
import com.plochem.ssa.economy.SEconomy;
import com.plochem.ssa.homes.Home;
import com.plochem.ssa.stats.StatsManager;

public class PlayerJoin implements Listener{
	private File playerFile;
	private YamlConfiguration playerData;
	private SSkyblockAddon sfa = SSkyblockAddon.getPlugin(SSkyblockAddon.class);
	private SEconomy sEco = sfa.getSEconomy();
	@EventHandler
	public void onJoin(PlayerJoinEvent e) throws IOException{
		Player joiner = e.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
            	if(!((OfflinePlayer)joiner).hasPlayedBefore()){
                	joiner.teleport(sfa.getSpawn());
                	Bukkit.dispatchCommand(joiner, "help");
            	}
        		StatsManager.showScoreboard(joiner);
            }
        }.runTaskLater(sfa, 1);
		
		// generates balance files
		playerFile = new File("plugins/SFA/playerbalance/" + joiner.getUniqueId().toString() + ".yml");
		playerData = YamlConfiguration.loadConfiguration(playerFile);
		if(!(playerFile.exists())) {
			Bukkit.getServer().getLogger().info("[SFA] Creating player balance file for " + joiner.getName() + "!");
			playerData.set("balance", 1000.00);
			playerData.save(playerFile);
			sEco.loadToMap(joiner.getUniqueId(), playerData.getDouble("balance"));
		}  else {
			Bukkit.getServer().getLogger().info("[SFA] Player balance file already exists for " + joiner.getName()+ "! Skipping creation...");
		}
		//generates booster files
		playerFile = new File("plugins/SFA/playerBoosters/" + joiner.getUniqueId().toString() + ".yml");
		playerData = YamlConfiguration.loadConfiguration(playerFile);
		if(!(playerFile.exists())) {
			Bukkit.getServer().getLogger().info("[SFA] Creating player booster file for " + joiner.getName() + "!");
			playerData.set("boosters", new ArrayList<Booster>());
			playerData.save(playerFile);
		}  else {
			Bukkit.getServer().getLogger().info("[SFA] Player booster file already exists for " + joiner.getName()+ "! Skipping creation...");
		}
		//generates kit cooldown files
		playerFile = new File("plugins/SFA/kitcooldowns/" + joiner.getUniqueId().toString() + ".yml");
		playerData = YamlConfiguration.loadConfiguration(playerFile);
		if(!(playerFile.exists())) {
			Bukkit.getServer().getLogger().info("[SFA] Creating player kit cooldown file for " + joiner.getName() + "!");
			playerData.save(playerFile);
		}  else {
			Bukkit.getServer().getLogger().info("[SFA] Player kit cooldown file already exists for " + joiner.getName()+ "! Skipping creation...");
		}

        // generate home files
		playerFile = new File("plugins/SFA/playerHomes/" + joiner.getUniqueId().toString() + ".yml");
		playerData = YamlConfiguration.loadConfiguration(playerFile);
		if(!(playerFile.exists())) {
			Bukkit.getServer().getLogger().info("[SFA] Creating player home file for " + joiner.getName() + "!");
			playerData.set("default", null);
			playerData.set("homes", new ArrayList<Home>());
			playerData.save(playerFile);
		}  else {
			Bukkit.getServer().getLogger().info("[SFA] Player home file already exists for " + joiner.getName()+ "! Skipping creation...");
		}
		
		// generatate perk files
		playerFile = new File("plugins/SFA/playerPerks/" + joiner.getUniqueId().toString() + ".yml");
		playerData = YamlConfiguration.loadConfiguration(playerFile);
		if(!(playerFile.exists())) {
			Bukkit.getServer().getLogger().info("[SFA] Creating player perk file for " + joiner.getName() + "!");
			playerData.save(playerFile);
		}  else {
			Bukkit.getServer().getLogger().info("[SFA] Player perk file already exists for " + joiner.getName()+ "! Skipping creation...");
		}
		
		// generatate stats files
		playerFile = new File("plugins/SFA/playerStats/" + joiner.getUniqueId().toString() + ".yml");
		playerData = YamlConfiguration.loadConfiguration(playerFile);
		if(!(playerFile.exists())) {
			Bukkit.getServer().getLogger().info("[SFA] Creating player stats file for " + joiner.getName() + "!");
			playerData.set("kills", 0);
			playerData.set("deaths", 0);
			playerData.save(playerFile);
		}  else {
			Bukkit.getServer().getLogger().info("[SFA] Player stats file already exists for " + joiner.getName()+ "! Skipping creation...");
		}
		
		playerFile = new File("plugins/SFA/seasons/playerdata/" + joiner.getUniqueId().toString() + ".yml");
		playerData = YamlConfiguration.loadConfiguration(playerFile);
		if(!(playerFile.exists())) {
			Bukkit.getServer().getLogger().info("[SFA] Creating player season file for " + joiner.getName() + "!");
			playerData.save(playerFile);
		}  else {
			Bukkit.getServer().getLogger().info("[SFA] Player season file already exists for " + joiner.getName()+ "! Skipping creation...");
		}
		
		playerFile = new File("plugins/SFA/cosmetics/playerdata/" + joiner.getUniqueId().toString() + ".yml");
		playerData = YamlConfiguration.loadConfiguration(playerFile);
		if(!(playerFile.exists())) {
			Bukkit.getServer().getLogger().info("[SFA] Creating player cosmetic file for " + joiner.getName() + "!");
			for(CosmeticType type : CosmeticType.values())
				playerData.set(type.toString(), "Default");
			playerData.save(playerFile);
		}  else {
			Bukkit.getServer().getLogger().info("[SFA] Player cosmetic file already exists for " + joiner.getName()+ "! Skipping creation...");
		}
		CosmeticManager.projectile.put(joiner.getUniqueId(), ProjectileTrail.valueOf(playerData.getString(CosmeticType.Projectile_Trail.toString())));
		CosmeticManager.trail.put(joiner.getUniqueId(), TrailEffect.valueOf(playerData.getString(CosmeticType.Trail_Effect.toString())));
		//
	}
}
