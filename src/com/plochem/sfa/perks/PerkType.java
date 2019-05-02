package com.plochem.sfa.perks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public enum PerkType {
	REGENERATION(new ItemStack(Material.GOLDEN_APPLE), 1000, 0){
		@Override
		public void performAction(Player source, Player target) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String[] buildDescription(Player viewer) {
			int level = PerkManager.currentLevel(this, viewer)+1;
			String[] desc = new String[] {"§7Gives you Regeneration I for", "§7" + (2*level) + " seconds."};
			return desc;
		}
	},
	DEFLECT(new ItemStack(Material.BARRIER), 1000, 3, 5){
		@Override
		public void performAction(Player source, Player target) {
						
		}

		@Override
		public String[] buildDescription(Player viewer) {
			int level = PerkManager.currentLevel(this, viewer)+1;
			String[] desc = new String[] {"§7" + 2*level + "§7% chance of deflecting damage", "§7back to your opponent"};
			return desc;
		}
	};
	
	private int unlockCost;
	private int chance;
	private ItemStack itemRep;
	private int maxLevel;
	
	private PerkType(ItemStack itemRep, int unlockCost, int maxLevel) {
		this.unlockCost = unlockCost;
		this.itemRep = itemRep;
		this.maxLevel = maxLevel;
	}
	
	private PerkType(ItemStack itemRep, int unlockCost, int maxLevel, int chance) {
		this.unlockCost = unlockCost;
		this.maxLevel = maxLevel;
		this.chance = chance;
		this.itemRep = itemRep;
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
	
	public int getMaxLevel() {
		return maxLevel;
	}
	
	public abstract void performAction(Player source, Player target);
	public abstract String[] buildDescription(Player viewer); // description tells you about the perks if you upgrade
}
