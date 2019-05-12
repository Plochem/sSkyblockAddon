package com.plochem.sfa.perks;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PerkManager {
	
	/**
	 * @param type - PerkType
	 * @param p - Target player
	 * @return 0 = Player doesn't own that perk
	 */
	public static int currentLevel(PerkType type, Player p) {
		File playerFile = new File("plugins/SFA/playerPerks/" + p.getUniqueId().toString() + ".yml");
		YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
		if(playerData.get(type.toString()) == null) {
			return 0;
		}
		return (Integer)playerData.get(type.toString());
	}
	
	public static int getNextLvlUpgrade(PerkType type, Player p) {
		return Math.min(PerkManager.currentLevel(type, p)+1, type.getMaxLevel());
	}
	
}
