package com.plochem.ssa.stats;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class Leaderboard {
	private Location loc;
	private List<String> text;
	private double gap = 0.35D;
	private List<ArmorStand> armorstands = new ArrayList<>();

	public Leaderboard(Location loc, String header, List<String>text) {
		this.loc = loc;
		this.text = text;
		text.add(0, "§b§l" + header);
	}

	public void show() {
		if(loc == null) return;
		Location displayLoc = loc.clone().add(0, (gap * text.size()) - 1.97D, 0);
		for (int i = 0; i < text.size(); i++) {
			ArmorStand as = (ArmorStand) displayLoc.getWorld().spawnEntity(displayLoc, EntityType.ARMOR_STAND);
			as.setGravity(false);
			as.setCanPickupItems(false);
			as.setCustomName(text.get(i));
			as.setCustomNameVisible(true);
			as.setVisible(false);
			armorstands.add(as);
			displayLoc.add(0, -gap, 0);
		}
	}
	
	public void delete() {
		for(ArmorStand as : armorstands) {
			as.remove();
		}
		armorstands.clear();
	}
}
