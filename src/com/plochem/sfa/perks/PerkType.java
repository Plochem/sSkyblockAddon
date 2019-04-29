package com.plochem.sfa.perks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public enum PerkType {
	REGEN(new ItemStack(Material.GOLDEN_APPLE), 1000, 3, new String[] {}){
		@Override
		void performAction(Player source, Player target) {
			// TODO Auto-generated method stub
			
		}
	},
	DEFLECT(new ItemStack(Material.BARRIER), 1000, 3, 5, new String[] {}){
		@Override
		void performAction(Player source, Player target) {
			// TODO Auto-generated method stub
			
		}
	}
	;
	
	private int unlockCost;
	private int chance;
	private ItemStack itemRep;
	private String[] description;
	private int maxLevel;
	
	private PerkType(ItemStack itemRep, int unlockCost, int maxLevel, String[] description) {
		this.unlockCost = unlockCost;
		this.itemRep = itemRep;
		this.maxLevel = maxLevel;
		this.description = description;
	}
	
	private PerkType(ItemStack itemRep, int unlockCost, int maxLevel, int chance, String[] description) {
		this.unlockCost = unlockCost;
		this.maxLevel = maxLevel;
		this.chance = chance;
		this.itemRep = itemRep;
		this.description = description;
	}
	
	public int getUnlockCost() {
		return unlockCost;
	}	
	
	public int getChance() {
		return chance;
	}
	
	public ItemStack getItemRep() {
		return itemRep;
	}
	
	public String[] getDescription() {
		return description;
	}
	
	public int getMaxLevel() {
		return maxLevel;
	}
	
	abstract void performAction(Player source, Player target);

}
