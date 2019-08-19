package com.plochem.ssa.cosmetics.types;

public enum KillEffect {
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

}
