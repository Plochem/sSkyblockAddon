package com.plochem.ssa.cosmetics.types;

import org.bukkit.Material;

public enum KillEffect implements Cosmetic{
	Default,
	Lightning_Bolt(5000);
	
	private int cost;
	
	private KillEffect(int cost) {
		this.cost = cost;
	}
	
	private KillEffect() {
	}
	
	public int getCost() {
		return cost;
	}

	@Override
	public Material getMaterial() {
		// TODO Auto-generated method stub
		return null;
	}

}
