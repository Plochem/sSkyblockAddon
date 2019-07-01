package com.plochem.ssa.generator;

public enum GeneratorType {
	COAL(30, 500000, "§8"),
	REDSTONE(30, 500000, "§4"),
	IRON_INGOT(40, 650000, "§f"),
	GOLD_INGOT(40, 650000, "§e"),
	EMERALD(50, 800000, "§a"),
	DIAMOND(60, 1000000, "§b");
	
	private int interval;
	private int cost;
	private String color;
	
	private GeneratorType(int interval, int cost, String color) {
		this.interval = interval;
		this.cost = cost;
		this.color = color;
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
}
