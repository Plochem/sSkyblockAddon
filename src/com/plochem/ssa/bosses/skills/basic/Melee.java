package com.plochem.ssa.bosses.skills.basic;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;

import com.plochem.ssa.bosses.BossEntity;
import com.plochem.ssa.bosses.skills.Skill;

import net.minecraft.server.v1_12_R1.DamageSource;

public class Melee extends Skill{

	public Melee() {
		super("Melee", 2, Arrays.asList("§7Deals the boss's damage to targetted player", "§7Damage is adjusted to your armor and potion", "§7effects."));
	}

	@Override
	public void cast(BossEntity boss, List<UUID> nearby) {
		((CraftPlayer)boss.getTarget()).getHandle().damageEntity(DamageSource.GENERIC, (float)boss.getStats().getDamage());
	}

}
