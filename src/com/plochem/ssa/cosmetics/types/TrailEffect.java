package com.plochem.ssa.cosmetics.types;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.plochem.ssa.SSkyblockAddon;
import com.plochem.ssa.cosmetics.CosmeticManager;

public enum TrailEffect implements Cosmetic{
	Default(Material.BARRIER) {

		@Override
		public void displayParticle(Player p) {
		}

	},
	Smoke_Aura(Material.INK_SACK, Effect.PARTICLE_SMOKE) {

		@Override
		public void displayParticle(Player p) {
			for(Effect e : this.getEffect()) {
				p.spigot().playEffect(p.getLocation(), e, 0, 0, (float)0.1, (float)0.4, (float)0.1, 0, 1, 100);	
			}		
		}

	},
	Flame_Aura(Material.TORCH, Effect.FLAME) {
		@Override
		public void displayParticle(Player p) {
			for(Effect e : this.getEffect()) {
				p.spigot().playEffect(p.getLocation(), e, 0, 0, (float)0.1, (float)0.4, (float)0.1, 0, 1, 100);	
			}				
		}

	},
	Enchantment_Aura(Material.ENCHANTMENT_TABLE, Effect.FLYING_GLYPH) {
		@Override
		public void displayParticle(Player p) {
			for(Effect e : this.getEffect()) {
				p.spigot().playEffect(p.getLocation(), e, 0, 0, (float)0.1, (float)0.4, (float)0.1, 0, 5, 100);	
			}			
		}

	};

	private Effect[] effect;
	private Material material;

	private TrailEffect(Material material, Effect... effect) {
		this.material = material;
		this.effect = effect;
	}

	public Effect[] getEffect() {
		return effect;
	}


	@Override
	public Material getMaterial() {
		return material;
	}
	
	public static void particleRunnable(Player p) {
		new BukkitRunnable() {
			@Override
			public void run() {
				TrailEffect trail = CosmeticManager.trail.get(p.getUniqueId());
				if(trail == TrailEffect.Default || trail == null || !p.isOnline()) {
					this.cancel();
					return;
				} else {
					trail.displayParticle(p);
				}
			}
		}.runTaskTimerAsynchronously(SSkyblockAddon.getPlugin(SSkyblockAddon.class), 0, 2);
	}


	public abstract void displayParticle(Player p);
}
