package com.plochem.ssa.generator;

public enum GeneratorType {
	COAL(4, 250000, "§8", 250),
	REDSTONE(4, 300000, "§4", 250),
	IRON(4, 400000, "§f", 250),
	GOLD(4, 450000, "§e", 250),
	DIAMOND(4, 550000, "§b", 250),
	EMERALD(4, 600000, "§a", 250);
	
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
