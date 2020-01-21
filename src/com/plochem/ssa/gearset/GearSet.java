package com.plochem.ssa.gearset;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.plochem.ssa.bosses.Tier;

public class GearSet {
	private List<ItemStack> items = new ArrayList<>();
	private Tier tier;
	private String name;
	private Ability ability;
	
	public GearSet(List<ItemStack> items, Tier tier, String name, Ability ability) {
		this.items = items;
		this.tier = tier;
		this.name = name;
		this.ability = ability;
	}
	
	public List<ItemStack> getItems() {
		return items;
	}
	
	public Tier getTier() {
		return tier;
	}

	public String getName() {
		return name;
	}

	public Ability getAbility() {
		return ability;
	}
	
}
