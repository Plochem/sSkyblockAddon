package com.plochem.ssa.cosmetics.types;

import org.bukkit.Material;
import org.bukkit.Sound;


public enum DeathSound implements Cosmetic{
	Default,
	Pig(5000, Sound.PIG_DEATH);
	
	private int cost;
	private Sound sound;
	
	private DeathSound(int cost, Sound sound) {
		this.cost = cost;
		this.sound = sound;
	}
	
	private DeathSound() {
	}
	
	public int getCost() {
		return cost;
	}

	public Sound getSound() {
		return sound;
	}

	@Override
	public Material getMaterial() {
		return null;
	}
}
