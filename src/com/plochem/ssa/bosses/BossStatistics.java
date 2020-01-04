package com.plochem.ssa.bosses;

public class BossStatistics {
	private double maxHealth;
	private double damage;
	private double health;
	private double defense;
	private double targetRadius;
	
	public BossStatistics(double maxHealth, double damage, double defense, double targetRadius) {
		this.maxHealth = maxHealth;
		this.damage = damage;
		this.defense = defense;
		this.health = maxHealth;
		this.targetRadius = targetRadius;
	}

	public double getMaxHealth() {
		return maxHealth;
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
	
	public double getTargetRadius() {
		return targetRadius;
	}
	
	public double decreaseHealth(double amount) {
		health -= amount;
		return health;
	}
	
}
