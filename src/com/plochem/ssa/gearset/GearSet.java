package com.plochem.ssa.gearset;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public class GearSet {
	private List<ItemStack> armor = new ArrayList<>();
	private List<ItemStack> items = new ArrayList<>();
	private String name;
	private Ability ability;
	
	public GearSet(List<ItemStack> armor, List<ItemStack> items, String name, Ability ability) {
		this.armor = armor;
		this.items = items;
		this.name = name;
		this.ability = ability;
	}

	public List<ItemStack> getArmor() {
		return armor;
	}
	
	public List<ItemStack> getItems() {
		return items;
	}

	public String getName() {
		return name;
	}

	public Ability getAbility() {
		return ability;
	}
	
}
