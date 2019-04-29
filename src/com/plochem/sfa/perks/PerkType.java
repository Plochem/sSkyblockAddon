package com.plochem.sfa.perks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public enum PerkType {
	REGEN(new ItemStack(Material.GOLDEN_APPLE), 1000, new String[] {}){
		@Override
		void performAction(Player source, Player target) {
			// TODO Auto-generated method stub
			
		}
	},
	DEFLECT(new ItemStack(Material.BARRIER), 1000, 5, new String[] {}){
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
	
	private PerkType(ItemStack itemRep, int unlockCost, String[] description) {
		this.unlockCost = unlockCost;
		this.itemRep = itemRep;
		this.description = description;
	}
	
	private PerkType(ItemStack itemRep, int unlockCost, int chance, String[] description) {
		this.unlockCost = unlockCost;
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
	abstract void performAction(Player source, Player target);

}
