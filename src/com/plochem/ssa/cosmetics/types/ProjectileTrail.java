package com.plochem.ssa.cosmetics.types;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;


public enum ProjectileTrail implements Cosmetic{
	Default (Material.BARRIER){
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {
		}
	},
	Black_Smoke(Material.FURNACE, Particle.SMOKE_NORMAL){
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {
			for(Particle e : this.getParticles()) {
				shooter.spawnParticle(e, projectile.getLocation(), 1, 0, 0, 0);
			}
		}

	},
	Water_Droplet(Material.WATER_BUCKET, Particle.DRIP_WATER) {
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {
			for(Particle e : this.getParticles()) {
				shooter.spawnParticle(e, projectile.getLocation(), 3, 0, 0, 0);
			}

		}
	},
	Flame(Material.TORCH, Particle.FLAME) {
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {
			for(Particle e : this.getParticles()) {
				shooter.spawnParticle(e, projectile.getLocation(), 1, 0, 0, 0);
			}
		}
	},
	Lava(Material.LAVA_BUCKET, Particle.LAVA) {
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {
			for(Particle e : this.getParticles()) {
				shooter.spawnParticle(e, projectile.getLocation(), 1, 0, 0, 0);		
			}
		}
	},
	Magic(Material.BREWING_STAND_ITEM, Particle.SPELL) {
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {
			for(Particle e : this.getParticles()) {
				shooter.spawnParticle(e, projectile.getLocation(), 1, 0, 0, 0);
			}
		}
	},
	Vampire(Material.INK_SACK, Particle.SMOKE_NORMAL, Particle.REDSTONE) {
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {
			for(Particle e : this.getParticles()) {
				shooter.spawnParticle(e, projectile.getLocation(), 1, 0, 0, 0);
			}
		}
	},
	Firework(Material.FIREWORK, Particle.FIREWORKS_SPARK) {
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {
			for(Particle e : this.getParticles()) {
				shooter.spawnParticle(e, projectile.getLocation(), 1, 0, 0, 0);
			}			
		}
	},
	Heart(Material.GOLDEN_APPLE, Particle.HEART) {
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {
			for(Particle e : this.getParticles()) {
				shooter.spawnParticle(e, projectile.getLocation(), 1, 0, 0, 0);
			}
		}
	},
	Color(Material.ARROW, Particle.REDSTONE) { //TODO
		@Override
		public void displayParticle(Projectile projectile, Player shooter) {
			for(Particle e : this.getParticles()) {

				shooter.spawnParticle(e, projectile.getLocation(), 0, 0.1, 0.1, 0.1);
			}
		}
	};

	private Particle[] particles;
	private Material material;
	public Particle[] getParticles() {
		return particles;
	}

	@Override
	public Material getMaterial() {
		return material;
	}


	private ProjectileTrail(Material material, Particle... particles) {
		this.particles = particles;
		this.material = material;
	}	

	public abstract void displayParticle(Projectile projectile, Player shooter);
}


