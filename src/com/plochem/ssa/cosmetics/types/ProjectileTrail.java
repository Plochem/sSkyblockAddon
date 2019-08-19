package com.plochem.ssa.cosmetics.types;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;


public enum ProjectileTrail implements Cosmetic{
	Default (Material.BARRIER){
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {			
		}
	},
	Black_Smoke(Material.FURNACE, Effect.PARTICLE_SMOKE){
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {
			for(Effect e : this.getParticles()) {
				shooter.spigot().playEffect(projectile.getLocation(), e, 0, 0, 0, 0, 0, 1, 1, 1);
			}
		}
		
	},
	Water_Droplet(Material.WATER, Effect.WATERDRIP) {
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {
			for(Effect e : this.getParticles()) {
				shooter.spigot().playEffect(projectile.getLocation(), e, 0, 0, 0, 0, 0, 1, 1, 1);
			}
			
		}
	},
	Flame(Material.TORCH, Effect.FLAME) {
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {
			for(Effect e : this.getParticles()) {
				shooter.spigot().playEffect(projectile.getLocation(), e, 0, 0, 0, 0, 0, 1, 1, 1);
			}
		}
	},
	Lava(Material.LAVA_BUCKET, Effect.LAVADRIP) {
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {
			for(Effect e : this.getParticles()) {
				shooter.spigot().playEffect(projectile.getLocation(), e, 0, 0, 0, 0, 0, 1, 1, 1);
			}
		}
	},
	Magic(Material.BREWING_STAND_ITEM, Effect.SPELL) {
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {
			for(Effect e : this.getParticles()) {
				shooter.spigot().playEffect(projectile.getLocation(), e, 0, 0, 0, 0, 0, 1, 1, 1);
			}
		}
	},
	Vampire(Material.INK_SACK, Effect.LARGE_SMOKE, Effect.COLOURED_DUST) {
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {
			for(Effect e : this.getParticles()) {
				shooter.spigot().playEffect(projectile.getLocation(), e, 0, 0, 0, 0, 0, 1, 1, 1);
			}
		}
	},
	Firework(Material.FIREWORK ,Effect.FIREWORKS_SPARK) {
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {
			for(Effect e : this.getParticles()) {
				shooter.spigot().playEffect(projectile.getLocation(), e, 0, 0, 0, 0, 0, 1, 1, 1);
			}			
		}
	},
	Heart(Material.GOLDEN_APPLE, Effect.HEART) {
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {
			for(Effect e : this.getParticles()) {
				shooter.spigot().playEffect(projectile.getLocation(), e, 0, 0, 0, 0, 0, 1, 1, 1);
			}
		}
	},
	Color(Material.ARROW, Effect.COLOURED_DUST) {
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {
			for(Effect e : this.getParticles()) {
				shooter.spigot().playEffect(projectile.getLocation(), e, 0, 0, 1, 1, 1, 1, 1, 1);
			}
		}
	};
	
	private Effect[] particles;
	private Material material;
	public Effect[] getParticles() {
		return particles;
	}
	
	@Override
	public Material getMaterial() {
		return material;
	}
	
	
	private ProjectileTrail(Material material, Effect... particles) {
		this.particles = particles;
		this.material = material;
	}	
	
	public abstract void displayParticle(Projectile projectile, Player shooter);
}


