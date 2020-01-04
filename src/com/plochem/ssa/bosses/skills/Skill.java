package com.plochem.ssa.bosses.skills;

import java.util.List;
import java.util.UUID;

import com.plochem.ssa.bosses.BossEntity;

public abstract class Skill {
	private String name;
	private List<String> description;
	
	public String getName() {
		return name;
	}
	
	public List<String> getDescription(){
		return description;
	}
	
	public abstract void cast(BossEntity boss, List<UUID> nearby);
}
