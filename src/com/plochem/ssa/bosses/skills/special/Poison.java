package com.plochem.ssa.bosses.skills.special;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.plochem.ssa.bosses.skills.Skill;
import com.plochem.ssa.bosses.skills.SkillHandler;

public class Poison implements SkillHandler{

	@Override
	public void castSkill(Skill skill, List<UUID> nearbyPlayers) {
		nearbyPlayers.forEach(id -> Bukkit.getPlayer(id).addPotionEffects(
		Arrays.asList(new PotionEffect(PotionEffectType.POISON, 20*10, 0),
					new PotionEffect(PotionEffectType.BLINDNESS, 20*10, 0))));
	}

}
