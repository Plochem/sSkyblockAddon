package com.plochem.ssa.cosmetics.types;

import org.bukkit.Effect;
import org.bukkit.Material;

public enum KillEffect implements Cosmetic{
	Default(Material.BARRIER),
	Lightning_Bolt(Material.ANVIL);
	
	private Material material;
	private Effect[] effect;
	
	private KillEffect(Material material, Effect...effect) {
		this.material = material;
		this.effect = effect;
	}
	
	public Effect[] getEffect() {
		return effect;
	}

	@Override
	public Material getMaterial() {
		return material;
	}

}
