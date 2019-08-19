package com.plochem.ssa.cosmetics.types;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public enum TrailEffect implements Cosmetic{
	Default(Material.BARRIER) {

		@Override
		public void displayParticle(Player p) {
		}

	},
	Smoke_Aura(Material.INK_SACK, Effect.SMOKE) {

		@Override
		public void displayParticle(Player p) {
			// TODO Auto-generated method stub
			
		}

	},
	Flame_Aura(Material.TORCH, Effect.FLAME) {

		@Override
		public void displayParticle(Player p) {
			// TODO Auto-generated method stub
			
		}

	},
	Enchantment_Aura(Material.ENCHANTMENT_TABLE, Effect.FLYING_GLYPH) {

		@Override
		public void displayParticle(Player p) {
			
		}

	};

	private Effect[] effect;
	private Material material;
	
	private TrailEffect(Material material, Effect... effect) {
		this.effect = effect;
	}
	
	public Effect[] getEffect() {
		return effect;
	}
	

	@Override
	public Material getMaterial() {
		return material;
	}
	
	public abstract void displayParticle(Player p);
}
