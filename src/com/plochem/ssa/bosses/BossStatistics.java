package com.plochem.ssa.bosses;

public class BossStatistics {
	private double health;
	private double damage;
	private double defense;
	
	public BossStatistics(double health, double damage, double defense) {
		this.health = health;
		this.damage = damage;
		this.defense = defense;
	}

	public double getHealth() {
		return health;
	}

	public double getDamage() {
		return damage;
	}

	public double getDefense() {
		return defense;
	}
	
	public double decreaseHealth(double amount) {
		health -= amount;
		return health;
	}
}
