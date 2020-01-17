package com.plochem.ssa.gearset;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GearSetManager {
	private static List<GearSet> gearsets = new ArrayList<>();
	static {
		gearsets.add(new GearSet(items, name, ability)) //TODO itembuilder with NBT methods
	}
	public static boolean isWearingSet(GearSet set, Player p) {
		for(ItemStack i : set.getItems()) {
			if(!p.getInventory().contains(i)) {
				
			}
		}
		// loop inventory. check if contains set.getITemStacks
		return false;
	}
	
	public static GearSet getGearSet(String s) {
		for(GearSet set : gearsets) {
			if(set.getName().equals(s)) {
				return set;
			}
		}
		return null;
	}
}
