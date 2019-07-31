package com.plochem.ssa.generator;

public enum GeneratorType {
	COAL(3, 250000, "§8", 7500),
	REDSTONE(4, 300000, "§4", 7500),
	IRON_INGOT(7, 400000, "§f", 7500),
	GOLD_INGOT(9, 450000, "§e", 7500),
	DIAMOND(11, 550000, "§b", 7500),
	EMERALD(15, 600000, "§a", 7500);
	
	private int interval; //sec
	private int cost;
	private String color;
	private int max;
	
	private GeneratorType(int interval, int cost, String color, int max) {
		this.interval = interval;
		this.cost = cost;
		this.color = color;
		this.max = max;
	}
	
	public int getInterval() {
		return interval;
	}
	
	public int getCost() {
		return cost;
	}
	
	public String getColor() {
		return color;
	}
	
	public int getMax() {
		return max;
	}
}
