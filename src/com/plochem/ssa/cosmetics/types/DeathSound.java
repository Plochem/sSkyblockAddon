package com.plochem.ssa.cosmetics.types;

import org.bukkit.Material;
import org.bukkit.Sound;


public enum DeathSound implements Cosmetic{
	Default (Material.BARRIER),
	Pig(Material.RAW_BEEF, Sound.PIG_DEATH);
	
	private Material material;
	private Sound[] sound;
	
	private DeathSound(Material material, Sound... sound) {
		this.material = material;
		this.sound = sound;
	}

	public Sound[] getSound() {
		return sound;
	}

	@Override
	public Material getMaterial() {
		return material;
	}
}
