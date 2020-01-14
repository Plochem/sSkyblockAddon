package com.plochem.ssa.bosses.skills;

import java.util.List;
import java.util.UUID;

import com.plochem.ssa.bosses.BossEntity;

public abstract class Skill {
	private String name;
	private double range;
	private List<String> description;
	
	public Skill(String name, double range, List<String> description) {
		this.name = name;
		this.range = range;
		this.description = description;
	}

	public String getName() {
		return name;
	}
	
	public List<String> getDescription(){
		return description;
	}
	
	public double getRange() {
		return range;
	}
	
	public abstract void cast(BossEntity boss, List<UUID> nearby);
}
