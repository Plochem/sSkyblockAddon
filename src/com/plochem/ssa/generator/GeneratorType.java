package com.plochem.ssa.generator;

public enum GeneratorType {
	COAL(30, 250000, "§8", 500),
	REDSTONE(30, 300000, "§4", 500),
	IRON_INGOT(40, 400000, "§f", 500),
	GOLD_INGOT(40, 450000, "§e", 500),
	DIAMOND(50, 550000, "§b", 500),
	EMERALD(60, 600000, "§a", 500);
	
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
