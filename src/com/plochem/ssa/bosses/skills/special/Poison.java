package com.plochem.ssa.bosses.skills.special;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.plochem.ssa.bosses.BossEntity;
import com.plochem.ssa.bosses.skills.Skill;

public class Poison extends Skill{

	@Override
	public void cast(BossEntity boss, List<UUID> nearby) {
		nearby.forEach(id -> Bukkit.getPlayer(id).addPotionEffects(
		Arrays.asList(new PotionEffect(PotionEffectType.POISON, 20*10, 0),
					new PotionEffect(PotionEffectType.BLINDNESS, 20*10, 0))));
	}

}
