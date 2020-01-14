package com.plochem.ssa.bosses;

public class BossStatistics {
	private double maxHealth;
	private double damage;
	private double defense;
	private double targetRadius;
	private int specialInterval;
	private int basicInterval;
	private int changeTargetInterval;
	private double blocksSecond;
	
	public BossStatistics(double maxHealth, double damage, double defense, double targetRadius, int specialInterval, int basicInterval, int changeTargetInterval, double blocksSecond) {
		this.maxHealth = maxHealth;
		this.damage = damage;
		this.defense = defense;
		this.maxHealth = maxHealth;
		this.targetRadius = targetRadius;
		this.specialInterval = specialInterval;
		this.basicInterval = basicInterval;
		this.changeTargetInterval = changeTargetInterval;
		this.blocksSecond = blocksSecond;
	}

	public double getMaxHealth() {
		return maxHealth;
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
	
	public int getSpecialInterval() {
		return specialInterval;
	}
	
	public int getBasicInterval() {
		return basicInterval;
	}
	
	public int getChangeTargetInterval() {
		return changeTargetInterval;
	}
	
	public double getSpeed() {
		return blocksSecond;
	}
	
}
