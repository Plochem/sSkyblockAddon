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
				shooter.spigot().playEffect(projectile.getLocation(), e, 0, 0, 0, 0, 0, 0, 0, 100);
			}
		}

	},
	Water_Droplet(Material.WATER_BUCKET, Effect.WATERDRIP) {
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {
			for(Effect e : this.getParticles()) {
				shooter.spigot().playEffect(projectile.getLocation(), e, 0, 0, 0, 0, 0, 0, 3, 100);
			}

		}
	},
	Flame(Material.TORCH, Effect.FLAME) {
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {
			for(Effect e : this.getParticles()) {
				shooter.spigot().playEffect(projectile.getLocation(), e, 0, 0, 0, 0, 0, 0, 0, 100);
			}
		}
	},
	Lava(Material.LAVA_BUCKET, Effect.LAVA_POP) {
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {
			for(Effect e : this.getParticles()) {
				shooter.spigot().playEffect(projectile.getLocation(), e, 0, 0, 0, 0, 0, 0, 0, 100);			
			}
		}
	},
	Magic(Material.BREWING_STAND_ITEM, Effect.SPELL) {
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {
			for(Effect e : this.getParticles()) {
				shooter.spigot().playEffect(projectile.getLocation(), e, 0, 0, 0, 0, 0, 0, 0, 100);
			}
		}
	},
	Vampire(Material.INK_SACK, Effect.PARTICLE_SMOKE, Effect.COLOURED_DUST) {
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {
			for(Effect e : this.getParticles()) {
				shooter.spigot().playEffect(projectile.getLocation(), e, 0, 0, 0, 0, 0, 0, 0, 100);
			}
		}
	},
	Firework(Material.FIREWORK ,Effect.FIREWORKS_SPARK) {
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {
			for(Effect e : this.getParticles()) {
				shooter.spigot().playEffect(projectile.getLocation(), e, 0, 0, 0, 0, 0, 0, 0, 100);
			}			
		}
	},
	Heart(Material.GOLDEN_APPLE, Effect.HEART) {
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {
			for(Effect e : this.getParticles()) {
				shooter.spigot().playEffect(projectile.getLocation(), e, 0, 0, 0, 0, 0, 0, 0, 100);
			}
		}
	},
	Color(Material.ARROW, Effect.COLOURED_DUST) {
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {
			for(Effect e : this.getParticles()) {
				shooter.spigot().playEffect(projectile.getLocation(), e, 0, 0, (float)0.1, (float)0.1, (float)0.1, 1, 2, 100);
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


