package com.plochem.ssa.cosmetics.types;

import org.bukkit.Material;
import org.bukkit.Particle;
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
	Smoke_Aura(Material.INK_SACK, Particle.SMOKE_NORMAL) {

		@Override
		public void displayParticle(Player p) {
			for(Particle e : this.getEffect()) {
				p.spawnParticle(e, p.getLocation(), 3, 0.1, 0.4, 0.1);
			}		
		}

	},
	Flame_Aura(Material.TORCH, Particle.FLAME) {
		@Override
		public void displayParticle(Player p) {
			for(Particle e : this.getEffect()) {
				p.spawnParticle(e, p.getLocation(), 3, 0.1, 0.4, 0.1);
			}				
		}

	},
	Enchantment_Aura(Material.ENCHANTMENT_TABLE, Particle.ENCHANTMENT_TABLE) {
		@Override
		public void displayParticle(Player p) {
			for(Particle e : this.getEffect()) {
				p.spawnParticle(e, p.getLocation(), 5, 0.1, 0.4, 0.1);
			}			
		}

	};

	private Particle[] effect;
	private Material material;

	private TrailEffect(Material material, Particle... effect) {
		this.material = material;
		this.effect = effect;
	}

	public Particle[] getEffect() {
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
