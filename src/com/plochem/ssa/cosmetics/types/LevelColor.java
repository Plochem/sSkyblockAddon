package com.plochem.ssa.cosmetics.types;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum LevelColor implements Cosmetic{
	Default,
	Green(1000, 35, ChatColor.GREEN);
	
	private ChatColor color;
	private int cost;
	private int minLevel;
	
	private LevelColor(int cost, int minLevel, ChatColor color) {
		this.color = color;
		this.cost = cost;
		this.minLevel = minLevel;
	}
	private LevelColor() {
	}
	
	public int getCost() {
		return cost;
	}
	
	public ChatColor getColor() {
		return color;
	}
	
	public int getMinLevel() {
		return minLevel;
	}
	@Override
	public Material getMaterial() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
