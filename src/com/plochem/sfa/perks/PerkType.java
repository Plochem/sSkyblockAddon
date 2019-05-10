package com.plochem.sfa.perks;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum PerkType {
	REGENERATION(new ItemStack(Material.GOLDEN_APPLE), 1000, 5){
		@Override
		public void performAction(LivingEntity source, LivingEntity target, int level) {
			((Player)source).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*3*level, 1)); // convert seconds to ticks
		}

		@Override
		public String[] buildDescription(Player viewer) {
			int level = PerkManager.getNextLvlUpgrade(this, viewer);
			String[] desc = new String[] {"§7Gives you Regeneration II for", "§7" + (3*level) + " seconds."};
			return desc;
		}
	},
	DEFLECT(new ItemStack(Material.BARRIER), 1000, 3, 5){
		@Override
		public void performAction(LivingEntity source, LivingEntity target, int level) {
			int num = new Random().nextInt(100) + 1;
			if (num <= level) {
				target.damage(((Player)source).getLastDamage(), source);
				target.sendMessage("§c" + ((Player)source).getName() + " deflected your attack!");
				source.sendMessage("§eYou deflected the attack because of your §bDeflect §eperk!");
			}
		}

		@Override
		public String[] buildDescription(Player viewer) {
			int level = PerkManager.getNextLvlUpgrade(this, viewer);
			String[] desc = new String[] {"§7" + level + "% chance of deflecting damage", "§7back to your opponent"};
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
	
	public abstract void performAction(LivingEntity source, LivingEntity target, int level);
	public abstract String[] buildDescription(Player viewer); // the description tells you about the perks if you upgrade
}
