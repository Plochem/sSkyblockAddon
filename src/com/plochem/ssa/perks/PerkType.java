package com.plochem.ssa.perks;

import java.util.Collection;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum PerkType {
	REGENERATION(new ItemStack(Material.GOLDEN_APPLE), 1000, 2, 5){
		@Override
		public void performAction(LivingEntity source, LivingEntity target, int level, Object...other) {
			((Player)source).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*3*level, 1)); // convert seconds to ticks
		}

		@Override
		public String[] buildDescription(Player viewer) {
			int level = PerkManager.getNextLvlUpgrade(this, viewer);
			String[] desc = new String[] {"§7Gives you Regeneration II for", "§7" + (3*level) + " seconds after killing a player."};
			return desc;
		}
	},
	FIRE_ARROW(new ItemStack(Material.BLAZE_POWDER), 2000, 2, 5){
		@Override
		public void performAction(LivingEntity source, LivingEntity target, int level, Object...other) {
			int num = new Random().nextInt(100) + 1;
			if (num <= (level*2)) {
				source.sendMessage("§eYou shot a flame arrow because of your §bFire Arrow §eperk!");
				((Arrow)other[0]).setFireTicks(1000);
			}
		}

		@Override
		public String[] buildDescription(Player viewer) {
			int level = PerkManager.getNextLvlUpgrade(this, viewer);
			String[] desc = new String[] {"§7" + level*2 + "% chance of firing a flame arrow", "§7instead of a normal one."};
			return desc;
		}
		
	},
	FORTUNE(new ItemStack(Material.DIAMOND_PICKAXE), 1000, 2, 5){
		@SuppressWarnings("unchecked")
		@Override
		public void performAction(LivingEntity source, LivingEntity target, int level, Object... other) {
			int num = new Random().nextInt(100) + 1;
			if (num <= (level*4)) {
				source.sendMessage("§eYou received an extra ore because of your §bFortune §eperk!");
				for(ItemStack i : (Collection<ItemStack>)other[0]){
					source.getWorld().dropItem(source.getLocation(), i);
				}
			}
			
		}

		@Override
		public String[] buildDescription(Player viewer) {
			int level = PerkManager.getNextLvlUpgrade(this, viewer);
			String[] desc = new String[] {"§7" + 4*level + "% chance of getting an extra", "§7drop from any mined ore."};
			return desc;
		}
		
	},
	JELLY_LEGS(new ItemStack(Material.FEATHER), 2000, 2, 5){

		@Override
		public void performAction(LivingEntity source, LivingEntity target, int level, Object... other) {
			EntityDamageEvent e = (EntityDamageEvent)(other[0]);
			double initial = e.getDamage();
			e.setDamage(e.getDamage() * (1-(level*0.2)));
			double last = Math.round(e.getDamage()*10.0)/10.0;
			source.sendMessage("§eYou took §c" + last + " §edamage instead of §c" + initial + " §ebecause of your §bJelly Legs §eperk.");
		}

		@Override
		public String[] buildDescription(Player viewer) {
			int level = PerkManager.getNextLvlUpgrade(this, viewer);
			String[] desc = new String[] {"§7Pemanently reduces fall damage by " + 20*level  + "%"};
			return desc; 
		}
		
	};
	
	private int unlockCost;
	private int chance;
	private ItemStack itemRep;
	private int maxLevel;
	private int costFactor;
	
	private PerkType(ItemStack itemRep, int unlockCost, int costFactor, int maxLevel) {
		this.unlockCost = unlockCost;
		this.itemRep = itemRep;
		this.maxLevel = maxLevel;
		this.costFactor = costFactor;
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
	
	public int getCostFactor() {
		return costFactor;
	}
	
	public int getCost(int currLevel) {
		return (int)(this.getUnlockCost() * Math.pow(this.getCostFactor(), currLevel));
	}
	
	public abstract void performAction(LivingEntity source, LivingEntity target, int level, Object... other);
	public abstract String[] buildDescription(Player viewer); // the description tells you about the perks if you upgrade
}
