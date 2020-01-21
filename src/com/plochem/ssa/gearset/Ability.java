package com.plochem.ssa.gearset;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

public abstract class Ability {
	private String name;
	private List<String> description;
	private int chance;
	
	public Ability(String name, int chance, List<String> description) {
		this.name = name;
		this.description = description;
		this.chance = chance;
	}

	public String getName() {
		return name;
	}
	
	public List<String> getDescription(){
		return description;
	}
	
	public int getChance() {
		return chance;
	}
	
	public abstract void execute(Player caster, List<UUID> targets);
}
