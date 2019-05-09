package com.plochem.sfa.perks;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum PerkType {
	REGENERATION(new ItemStack(Material.GOLDEN_APPLE), 1000, 1){
		@Override
		public void performAction(Entity source, Entity target, int level) {
			((Player)source).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 2*level, 1));
		}

		@Override
		public String[] buildDescription(Player viewer) {
			int level = PerkManager.getNextLvlUpgrade(this, viewer);
			String[] desc = new String[] {"�7Gives you Regeneration I for", "�7" + (2*level) + " seconds."};
			return desc;
		}
	},
	DEFLECT(new ItemStack(Material.BARRIER), 1000, 3, 5){
		@Override
		public void performAction(Entity source, Entity target, int level) {
						
		}

		@Override
		public String[] buildDescription(Player viewer) {
			int level = PerkManager.getNextLvlUpgrade(this, viewer);
			String[] desc = new String[] {"�7" + level + "% chance of deflecting damage", "�7back to your opponent"};
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
	
	public abstract void performAction(Entity source, Entity target, int level);
	public abstract String[] buildDescription(Player viewer); // the description tells you about the perks if you upgrade
}
