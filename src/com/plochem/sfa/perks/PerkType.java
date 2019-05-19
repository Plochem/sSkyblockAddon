package com.plochem.sfa.perks;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.plochem.sfa.SFactionAddon;

public enum PerkType {
	REGENERATION(new ItemStack(Material.GOLDEN_APPLE), 1000, 2, 5){
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
	DEFLECT(new ItemStack(Material.BARRIER), 5000, 4, 3){
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
	},
	FREEZE(new ItemStack(Material.ICE), 2000, 3, 3){
		@Override
		public void performAction(LivingEntity source, LivingEntity target, int level) {
			int num = new Random().nextInt(100) + 1;
			if (num <= level) {
				Player to = (Player)target;
				int duration = level*2;
				source.sendMessage("§eYour attack froze " +  to.getName() + " because of your §bFreeze §eperk!");
				to.sendMessage("§c" + source.getName() + "'s attack froze you for " + duration + " seconds!");
				PerkManager.frozenPlayers.add(to.getUniqueId());
				new BukkitRunnable() {
				    @Override
				    public void run() {
				    	PerkManager.frozenPlayers.remove(to.getUniqueId());
				    	to.sendMessage("§eYou are now able to move again!");
				       	this.cancel();
				    }
				}.runTaskTimer(SFactionAddon.getPlugin(SFactionAddon.class), duration*20, 0);
			}
		}

		@Override
		public String[] buildDescription(Player viewer) {
			int level = PerkManager.getNextLvlUpgrade(this, viewer);
			String[] desc = new String[] {"§7" + level + "% chance of freezing a player", " §7for " + level*2 + " §7seconds"};
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
	
	public abstract void performAction(LivingEntity source, LivingEntity target, int level);
	public abstract String[] buildDescription(Player viewer); // the description tells you about the perks if you upgrade
}
