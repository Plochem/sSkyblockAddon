package com.plochem.ssa.bosses;

public enum Tier {
	RARE("§a§l", "§2§l[§a§lRARE§2§l]"),
	EPIC("§d§l", "§5§l[§d§lEPIC§5§l]"),
	LEGENDARY("§6§l", "§6§l[§e§lLEGENDARY§6§l]");
	
	private String color;
	private String tag;
	
	private Tier(String color, String tag) {
		this.color = color;
		this.tag = tag;
	}
	
	public String getColor() {
		return color;
	}
	
	public String getTag() {
		return tag;
	}
}
