package com.plochem.sfa.stats;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class StatsListener implements Listener {
	@EventHandler
	public void onKill(PlayerDeathEvent e) throws IOException {
		Player killer = e.getEntity().getKiller();
		Player killed = e.getEntity();
		File playerFile = new File("plugins/SFA/playerStats/" + killed.getUniqueId().toString() + ".yml");
		YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
		playerData.set("deaths", playerData.getInt("deaths") + 1);
		playerData.save(playerFile);
		if(killer != null) {
			playerFile = new File("plugins/SFA/playerStats/" + killer.getUniqueId().toString() + ".yml");
			playerData = YamlConfiguration.loadConfiguration(playerFile);
			playerData.set("kills", playerData.getInt("kills") + 1);
			playerData.save(playerFile);
		}
	}
}
