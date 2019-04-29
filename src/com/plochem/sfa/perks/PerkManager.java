package com.plochem.sfa.perks;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PerkManager {
	public static int PURCHASE_SCALE_FACTOR = 2;
	
	public static int currentLevel(PerkType type, Player p) {
		File playerFile = new File("plugins/SFA/playerPerks/" + p.getUniqueId().toString() + ".yml");
		YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
		if(playerData.get(type.toString()) == null) {
			return 0;
		}
		return (Integer)playerData.get(type.toString());
	}
}
