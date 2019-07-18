package com.plochem.ssa.generator;

public enum GeneratorType {
	COAL(30, 250000, "§8"),
	REDSTONE(30, 300000, "§4"),
	IRON_INGOT(40, 400000, "§f"),
	GOLD_INGOT(40, 450000, "§e"),
	DIAMOND(50, 550000, "§b"),
	EMERALD(60, 600000, "§a");
	
	private int interval; //sec
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
